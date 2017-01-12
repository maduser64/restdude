/**
 *
 * Restdude
 * -------------------------------------------------------------------
 * Module restdude-error, https://manosbatsis.github.io/restdude/restdude-error
 *
 * Full stack, high level framework for horizontal, model-driven application hackers.
 *
 * Copyright © 2005 Manos Batsis (manosbatsis gmail)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.restdude.domain.error.resolver;

import com.restdude.domain.error.model.SystemError;
import com.restdude.domain.error.service.SystemErrorService;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.core.Ordered;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.util.WebUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Renders a response with, and optionally persists,  a RESTful Error representation based on the {@link SystemError} POJO.
 * Persistence of SystemError objects is optional and can be configured using restdude.validationErrors.system.persist* configuration properties.
 * <p>
 * At a high-level, this implementation functions as follows:
 * <p>
 * <ol>
 * <li>Upon encountering an Exception, the configured {@link RestErrorResolver} is consulted to resolve the
 * exception into a {@link SystemError} instance.</li>
 * <li>The HTTP Response's Status Code will be set to the {@code RestError}'s
 * {@link SystemError#getHttpStatusCode()} value.</li>
 * <li>The {@code RestError} instance is presented to a configured {@link RestErrorConverter} to allow transforming
 * the {@code RestError} instance into an object potentially more suitable for rendering as the HTTP response body.</li>
 * <li>If no {@code HttpMessageConverter}s {@code canWrite} the result object, nothing is done, and this handler
 * returns {@code null} to indicate other ExceptionResolvers potentially further in the resolution chain should
 * handle the exception instead.</li>
 * </ol>
 * <p>
 * <h3>Defaults</h3>
 * This implementation has the following property defaults:
 * <table>
 * <tr>
 * <th>Property</th>
 * <th>Instance</th>
 * <th>Notes</th>
 * </tr>
 * <tr>
 * <td>errorResolver</td>
 * <td>{@link DefaultRestErrorResolver DefaultRestErrorResolver}</td>
 * <td>Converts Exceptions to {@link SystemError} instances.  Should be suitable for most needs.</td>
 * </tr>
 * </table>
 * @see DefaultRestErrorResolver
 * @see HttpMessageConverter
 * @see org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
 */

public class RestExceptionHandler extends DefaultErrorAttributes implements ErrorAttributes, HandlerExceptionResolver, Ordered, InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestExceptionHandler.class);

    private SystemErrorService systemErrorService;
    private RestErrorResolver errorResolver;
    private RestErrorConverter<?> errorConverter;
    private RequestMappingHandlerAdapter requestMappingHandlerAdapter;
    private int order = Ordered.HIGHEST_PRECEDENCE;

    //leverage Spring's existing default setup behavior:
    private static final class HttpMessageConverterHelper extends WebMvcConfigurationSupport {
        public void addDefaults(List<HttpMessageConverter<?>> converters) {
            addDefaultHttpMessageConverters(converters);
        }
    }

    //@ExceptionHandler({Exception.class})
    //public SystemError handleExceptionAsControllerAdvice(HttpServletRequest request, HttpServletResponse response, Exception originalException) {
    //    ServletWebRequest webRequest = new ServletWebRequest(request, response);
    //    SystemError error = buildSystemError(webRequest, null, originalException);
    //    return error;
    //}
    /**
     * Actually resolve the given exception that got thrown during on handler execution, returning a ModelAndView that
     * represents a specific error page if appropriate.
     * <p/>
     * May be overridden in subclasses, in order to apply specific
     * exception checks. Note that this template method will be invoked <i>after</i> checking whether this resolved applies
     * ("mappedHandlers" etc), so an implementation may simply proceed with its actual exception handling.
     *
     * @param request  current HTTP request
     * @param response current HTTP response
     * @param handler  the executed handler, or <code>null</code> if none chosen at the time of the exception (for example,
     *                 if multipart resolution failed)
     * @param originalException       the exception that got thrown during handler execution
     * @return a corresponding ModelAndView to forward to, or <code>null</code> for default processing
     */
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception originalException) {
        ModelAndView mav = null;
        Map<String, Object> errorAttributes = this.getErrorAttributes(new ServletRequestAttributes(request, response), true);
        LOGGER.debug("doResolveException, errorAttributes: {}", errorAttributes);

        try {

            ServletWebRequest webRequest = new ServletWebRequest(request, response);
            SystemError error = buildSystemError(webRequest, handler, originalException);

            if (error != null) {
                mav = getModelAndView(webRequest, handler, error);
            }

        } catch (Exception invocationEx) {
            LOGGER.error("Failed handling exception", originalException);
            RuntimeException wrapperErxception = new RuntimeException("Failed handling original exception with message [" + originalException.getMessage() + "]", invocationEx);
            wrapperErxception.addSuppressed(originalException);
            throw wrapperErxception;
        }
        return mav;
    }

    private SystemError buildSystemError(ServletWebRequest webRequest, Object handler, Exception originalException) {
        RestErrorResolver resolver = getErrorResolver();
        SystemError error = resolver.resolveError(webRequest, handler, originalException);
        if (error != null) {
            LOGGER.error("doResolveException, error stacktrace:", error.getThrowable());
            try {
                // persist?
                if (true) {
                    error = this.systemErrorService.create(error);
                }
            } catch (Exception e) {
                LOGGER.error("Failed persisting error", e);
            }

            // apply response status
            applyStatusIfPossible(webRequest, error);
            // apply response headers
            applyHeadersIfPossible(webRequest, error);
        }
        return error;
    }

    protected ModelAndView getModelAndView(ServletWebRequest webRequest, Object handler, SystemError error) throws Exception {

        // set the error as default body to handle
        // cases where no converter is configured
        Object body = error;

        // apply converter if any
        RestErrorConverter converter = getErrorConverter();
        if (converter != null) {
            body = converter.convert(error);
        }

        // handle response
        return handleResponseBody(body, webRequest);
    }

    private void applyStatusIfPossible(ServletWebRequest webRequest, SystemError error) {
        if (!WebUtils.isIncludeRequest(webRequest.getRequest())) {
            webRequest.getResponse().setStatus(error.getHttpStatusCode().intValue());
        }
    }

    private void applyHeadersIfPossible(ServletWebRequest webRequest, SystemError error) {
        if (!WebUtils.isIncludeRequest(webRequest.getRequest())
                && MapUtils.isNotEmpty(error.getResponseHeaders())) {

            Map<String, String> resHeaders = error.getResponseHeaders();

            HttpServletResponse response = webRequest.getResponse();
            for (String headerName : resHeaders.keySet()) {
                response.addHeader(headerName, resHeaders.get(headerName));
            }
        }
    }

    private ModelAndView handleResponseBody(Object body, ServletWebRequest webRequest) throws ServletException, IOException {
        LOGGER.error("handleResponseBody");

        HttpInputMessage inputMessage = new ServletServerHttpRequest(webRequest.getRequest());

        List<MediaType> acceptedMediaTypes = inputMessage.getHeaders().getAccept();
        if (acceptedMediaTypes.isEmpty()) {
            acceptedMediaTypes = Collections.singletonList(MediaType.ALL);
        }

        MediaType.sortByQualityValue(acceptedMediaTypes);
        HttpOutputMessage outputMessage = new ServletServerHttpResponse(webRequest.getResponse());
        Class<?> bodyType = body.getClass();
        List<HttpMessageConverter<?>> converters = this.requestMappingHandlerAdapter.getMessageConverters();

        if (converters != null) {
            for (MediaType acceptedMediaType : acceptedMediaTypes) {
                for (HttpMessageConverter messageConverter : converters) {
                    if (messageConverter.canWrite(bodyType, acceptedMediaType)) {
                        messageConverter.write(body, acceptedMediaType, outputMessage);
                        //return empty model and view to let
                        //Spring know a view has already been rendered
                        return new ModelAndView();
                    }
                }
            }
        }

        LOGGER.warn("No suitable HttpMessageConverter for class: {} and MIME: {}", bodyType.getCanonicalName(), acceptedMediaTypes);
        return null;
    }


    @Autowired
    public void setRequestMappingHandlerAdapter(RequestMappingHandlerAdapter requestMappingHandlerAdapter) {
        this.requestMappingHandlerAdapter = requestMappingHandlerAdapter;
    }

    @Autowired
    public void setSystemErrorService(SystemErrorService systemErrorService) {
        this.systemErrorService = systemErrorService;
    }

    public RestExceptionHandler() {
        this.errorResolver = new DefaultRestErrorResolver();
    }

    @Override
    public int getOrder() {
        return this.order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public void setErrorResolver(RestErrorResolver errorResolver) {
        this.errorResolver = errorResolver;
    }

    public RestErrorResolver getErrorResolver() {
        return this.errorResolver;
    }

    public RestErrorConverter<?> getErrorConverter() {
        return errorConverter;
    }

    public void setErrorConverter(RestErrorConverter<?> errorConverter) {
        this.errorConverter = errorConverter;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        LOGGER.info("Configured as HandlerExceptionResolver");
    }
}