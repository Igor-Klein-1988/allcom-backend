package de.allcom.services.mail;


import freemarker.template.Configuration;
import freemarker.template.Template;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

@Component
@RequiredArgsConstructor
public class MailTemplatesUtil {

    private final Configuration freemarkerConfiguration;

    public String createRegistrationMail(String firstName, String lastName) {
        try {
            Template template = freemarkerConfiguration.getTemplate("registration_mail.ftlh");

            Map<String, Object> model = new HashMap<>();
            model.put("firstName", firstName);
            model.put("lastName", lastName);

            return FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public String createActivatedMail(String firstName, String lastName) {
        try {
            Template template = freemarkerConfiguration.getTemplate("activatedAccount_mail.ftlh");

            Map<String, Object> model = new HashMap<>();
            model.put("firstName", firstName);
            model.put("lastName", lastName);

            return FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public String createBlocketedMail(String firstName, String lastName) {
        try {
            Template template = freemarkerConfiguration.getTemplate("blockedAccount_mail.ftlh");

            Map<String, Object> model = new HashMap<>();
            model.put("firstName", firstName);
            model.put("lastName", lastName);

            return FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
