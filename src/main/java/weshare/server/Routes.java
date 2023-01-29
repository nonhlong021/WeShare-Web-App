package weshare.server;

import weshare.controller.*;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.post;

public class Routes {
    public static final String LOGIN_PAGE = "/";
    public static final String LOGIN_ACTION = "/login.action";
    public static final String LOGOUT = "/logout";
    public static final String EXPENSES = "/expenses";
    public static final String PAYMENT_RECEIVED = "/paymentrequests_received";
    public static final String PAYMENT_REQUEST = "/paymentrequest";
    public static final String PAYMENTREQUESTACTION = "/paymentrequest.action";
    public static final String PAYMENT_SENT = "/paymentrequests_sent";
    public static final String ADD_EXPENSE_LINK = "/newexpense";
    public static final String NEW_EXPENSE_ACTION = "/expense.action";
    public static final String NEW_PAYMENT_ACTION = "/payment.action";




    public static void configure(WeShareServer server) {

        server.routes(() -> {
            post(LOGIN_ACTION,  PersonController.login);
            post(NEW_EXPENSE_ACTION,        ExpensesController.AddNewExpense);
            post(NEW_PAYMENT_ACTION, PaymentReceivedController.payReceived);
            get(LOGOUT,         PersonController.logout);
            get(EXPENSES,           ExpensesController.view);
            get(PAYMENT_RECEIVED,   PaymentReceivedController.viewReceived);
            get(PAYMENT_SENT,       PaymentSentController.viewSent);
            get(ADD_EXPENSE_LINK, AddNewExpenseController.view);
            get(PAYMENT_REQUEST, PaymentRequestController.paymentParameter);
            post(PAYMENTREQUESTACTION,      PaymentRequestActionController.paymentAction);
        });
    }
}
