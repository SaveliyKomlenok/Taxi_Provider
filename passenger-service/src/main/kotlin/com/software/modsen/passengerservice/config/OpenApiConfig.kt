package com.software.modsen.passengerservice.config

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Contact
import io.swagger.v3.oas.annotations.info.Info

@OpenAPIDefinition(
    info = Info(
        title = "Taxi provider Api",
        description = "provide taxi",
        version = "1.0.0",
        contact = Contact(
            name = "Komlenok Saveliy",
            email = "skomlenok363@gmail.com",
            url = "https://github.com/SaveliyKomlenok"
        )
    )
)
class OpenApiConfig 