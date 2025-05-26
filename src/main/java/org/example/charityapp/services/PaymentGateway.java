package org.example.charityapp.services;

import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.example.charityapp.entities.Donation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentGateway {
    @Value("${stripe.api.key}")
    private String stripeApiKey;

    /**
     * Creates a Stripe Checkout Session and returns the session URL for redirect.
     * @param donation The donation entity containing amount and description.
     * @param successUrl The URL to redirect to after successful payment.
     * @param cancelUrl The URL to redirect to if payment is cancelled.
     * @return The URL to Stripe Checkout.
     * @throws Exception if Stripe API call fails.
     */
    public String createStripeCheckoutSession(Donation donation, String successUrl, String cancelUrl) throws Exception {
        Stripe.apiKey = stripeApiKey;
        long amountInCents = donation.getAmount().multiply(java.math.BigDecimal.valueOf(100)).longValue();
        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(successUrl)
                .setCancelUrl(cancelUrl)
                .addLineItem(
                    SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(
                            SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("usd")
                                .setUnitAmount(amountInCents)
                                .setProductData(
                                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName("Donation to " + (donation.getCharityAction() != null ? donation.getCharityAction().getTitle() : "Charity"))
                                        .build()
                                )
                                .build()
                        )
                        .build()
                )
                .build();
        Session session = Session.create(params);
        return session.getUrl();
    }

    public void processPayment(Donation donation) {
        // Deprecated: Use createStripeCheckoutSession instead for Stripe payments
    }

    public void refundPayment(Donation donation) {
        // Logic to refund a donation
    }
}

