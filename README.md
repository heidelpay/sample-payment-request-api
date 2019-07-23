# Payment Request Api - Sample Payment Method
A simple Spring Boot app used as a example for an article on [W3C's Payment Request Api Specification](https://www.w3.org/TR/payment-request) written at the [javamagazin](https://jaxenter.de/ausgaben/java-magazin-9-19)

As an sample heidelpay's Flexipay invoice is integrated into the Payment-Request-Api. To see it in action use the [sample shop](https://github.com/heidelpay/sample-shop-spring-boot).

## RUN
Assuming Java (>= 8) is installed on your system, simply clone the repo and call `gradlew bootRun`. The Payment-methods will run under http://localhost:8081/heidelpay-invoice
