import com.stripe.Stripe;
import com.stripe.exception.*;
import com.stripe.model.Customer;
import com.stripe.model.Invoice;
import com.stripe.model.InvoiceItem;

import java.util.HashMap;
import java.util.Map;

public class StripeClient {

    public StripeClient() {
        Stripe.apiKey = "YOUR_API_KEY";
    }

    public Customer createCustomer(User user, String paymentToken) throws CardException, APIException, AuthenticationException, InvalidRequestException, APIConnectionException {
        Map<String, Object> customerParams = new HashMap<String, Object>();
        customerParams.put("description", user);
        customerParams.put("source", paymentToken);
        Customer customer = Customer.create(customerParams);
        user.setStripeCustomerId(customer.getId());
        //  TODO : Update user
        // Update user

        return customer;
    }

    public void updateCustomerCard(User user, String paymentToken) throws CardException, APIException, AuthenticationException, InvalidRequestException, APIConnectionException {
        Customer customer = Customer.retrieve(user.getStripeCustomerId());
        Map<String, Object> updateParams = new HashMap<String, Object>();
        updateParams.put("source", paymentToken);
        customer.update(updateParams);
    }

    public InvoiceItem createInvoiceItem(int amount, User user, String description) throws CardException, APIException, AuthenticationException, InvalidRequestException, APIConnectionException {
        Map<String, Object> invoiceItemParams = new HashMap<String, Object>();
        invoiceItemParams.put("customer", user.getStripeCustomerId());
        invoiceItemParams.put("amount", amount);
        invoiceItemParams.put("currency", "usd");
        invoiceItemParams.put("description", description);

        return InvoiceItem.create(invoiceItemParams);
    }

    public Invoice createInvoice(User user, boolean payImmediately) throws CardException, APIException, AuthenticationException, InvalidRequestException, APIConnectionException {
        Map<String, Object> invoiceParams = new HashMap<String, Object>();
        invoiceParams.put("customer", user.getStripeCustomerId());

        Invoice invoice = Invoice.create(invoiceParams);

        if (payImmediately)
            invoice.pay();

        return invoice;
    }
}
