package com.charalambos.pharmaciescy.Support;

import static java.lang.System.lineSeparator;

public class EmailBody {
    private final String messageFinal;

    public EmailBody(EmailBodyBuilder emailBodyBuilder) {
        messageFinal = "Name: " +
                emailBodyBuilder.name +
                lineSeparator() +
                "Email: " +
                emailBodyBuilder.email +
                lineSeparator() +
                "Phone: " +
                emailBodyBuilder.phone +
                lineSeparator() +
                lineSeparator() +
                "Μύνημα: " +
                lineSeparator() +
                emailBodyBuilder.message;
    }

    public String getMessageFinal() {
        return messageFinal;
    }

    public static class EmailBodyBuilder {
        private String name;
        private String phone;
        private String email;
        private String message;

        public EmailBodyBuilder() {
        }

        public EmailBodyBuilder setName(String name) {
            String[] temp = name.trim().split(" ");
            String[] out = new String[temp.length];
            for (int i=0; i<temp.length; i++) {
                out[i] = temp[i].substring(0, 1).toUpperCase() + temp[i].substring(1);
            }
            this.name = String.join(" ",out);
            return this;
        }

        public EmailBodyBuilder setPhone(String phone) {
            this.phone = phone.trim();
            return this;
        }

        public EmailBodyBuilder setEmail(String email) {
            this.email = email.trim();
            return this;
        }

        public EmailBodyBuilder setMessage(String message) {
            this.message = message.trim();
            return this;
        }

        public EmailBody build() {
            return new EmailBody(this);
        }
    }

}
