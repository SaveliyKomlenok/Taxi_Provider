package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "Passenger client"

    request {
        url "/api/v1/passengers/1"
        method GET()
    }

    response {
        status OK()
        headers {
            contentType applicationJson()
        }
        body(
                "id": 1,
                "firstname": "Джон",
                "surname": "Уильямс",
                "patronymic": "Джонсон",
                "email": "test@example.com",
                "phoneNumber": "+375331234567",
                "isRestricted": false
        )
    }
}
