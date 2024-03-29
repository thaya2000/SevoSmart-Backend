package org.sevosmart.com.sevosmartbackend.utils;

import org.sevosmart.com.sevosmartbackend.model.Guest;

public class EmailUtils {
    public static String getEmailMessage(Guest guest) {
        return "Consultation details" + "\n\nClient Details" + "\n\n Name : " + guest.getFirstName() + " "
                + guest.getLastName() +
                "\n\n Phone Number : " + guest.getPhoneNo() +
                "\n\n Email : " + guest.getEmail();
    }
}
