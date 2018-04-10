import com.stripe.exception.*;

public class Main {
    public static void main(String[] args) {
        try {
            chargeCustomer("tok_mastercard", new StripeClient());
        } catch (CardException | APIException | InvalidRequestException | AuthenticationException | APIConnectionException e) {
            e.printStackTrace();
        }
    }

    private static void chargeCustomer(String token, StripeClient stripeClient) throws CardException, APIException, AuthenticationException, InvalidRequestException, APIConnectionException {
        User user = new User();
        user.setId(1);
        if (user.getStripeCustomerId() == null || user.getStripeCustomerId().isEmpty()) {
            stripeClient.createCustomer(user, token);
        } else {
            stripeClient.updateCustomerCard(user, token);
        }

        stripeClient.createInvoiceItem(
                5 * 100,
                user,
                "Your Description");

        // TODO: 4/9/2018

        stripeClient.createInvoice(user, true);

    }
}
