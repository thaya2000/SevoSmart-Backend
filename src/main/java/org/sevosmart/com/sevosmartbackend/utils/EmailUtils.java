package org.sevosmart.com.sevosmartbackend.utils;

import org.sevosmart.com.sevosmartbackend.model.Consultation;

public class EmailUtils {
    public static String getEmailMessage(Consultation consultation) {
        return
                "\n\n Name : " + consultation.getFirstName() + " " + consultation.getLastName() +
                "\n\n Phone Number : " + consultation.getPhoneNo() +
                "\n\n Address : " + consultation.getAddress() +
                "\n\n Email : " + consultation.getEmail()
                + "\n\n Category : " + consultation.getCategory()
                + "\n\n Choose Product : " + consultation.getChooseProduct()
                + "\n\n Message : " + consultation.getMessage();
    }
}
