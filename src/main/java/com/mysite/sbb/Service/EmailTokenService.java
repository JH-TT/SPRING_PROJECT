package com.mysite.sbb.Service;

import com.mysite.sbb.Model.EmailToken;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface EmailTokenService {

    String createEmailToken(String userEmail, String receiverEmail) throws MessagingException, UnsupportedEncodingException;
    EmailToken findByIdAndExpirationDateAfterAndExpired(String emailTokenId);
}
