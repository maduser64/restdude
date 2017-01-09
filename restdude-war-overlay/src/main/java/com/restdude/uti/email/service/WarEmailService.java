/**
 * Restdude
 * -------------------------------------------------------------------
 * Module restdude-war-overlay, https://manosbatsis.github.io/restdude/restdude-war-overlay
 * <p>
 * Full stack, high level framework for horizontal, model-driven application hackers.
 * <p>
 * Copyright © 2005 Manos Batsis (manosbatsis gmail)
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.restdude.uti.email.service;


import com.restdude.util.email.service.AbstractEmailService;
import com.restdude.util.email.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

import javax.inject.Inject;

@Service
public class WarEmailService extends AbstractEmailService implements EmailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WarEmailService.class);


    @Value("${mail.server.from}")
    private String defaultMailFrom;

    @Value("${mail.enabled}")
    private Boolean mailEnabled;

    @Value("${restdude.baseurl}")
    private String baseUrl;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Inject
    @Qualifier("messageSource")
    private MessageSource messageSource;

    public String getDefaultMailFrom() {
        return defaultMailFrom;
    }

    public Boolean getMailEnabled() {
        return mailEnabled;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public JavaMailSender getMailSender() {
        return mailSender;
    }

    public TemplateEngine getTemplateEngine() {
        return templateEngine;
    }

    public MessageSource getMessageSource() {
        return messageSource;
    }


}