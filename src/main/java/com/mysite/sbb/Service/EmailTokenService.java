package com.mysite.sbb.Service;

import com.mysite.sbb.Model.EmailToken;

public interface EmailTokenService {

    String createEmailToken(String userEmail, String receiverEmail);
    EmailToken findByIdAndExpirationDateAfterAndExpired(String emailTokenId);
}
