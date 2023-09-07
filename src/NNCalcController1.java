import components.naturalnumber.NaturalNumber;
import components.naturalnumber.NaturalNumber2;

/**
 * Controller class.
 *
 * @author Aidan Whitlatch
 */
public final class NNCalcController1 implements NNCalcController {

    /**
     * Model object.
     */
    private final NNCalcModel model;

    /**
     * View object.
     */
    private final NNCalcView view;

    /**
     * Useful constants.
     */
    private static final NaturalNumber TWO = new NaturalNumber2(2),
            INT_LIMIT = new NaturalNumber2(Integer.MAX_VALUE);

    /**
     * Updates this.view to display this.model, and to allow only operations
     * that are legal given this.model.
     *
     * @param model
     *            the model
     * @param view
     *            the view
     * @ensures [view has been updated to be consistent with model]
     */
    private static void updateViewToMatchModel(NNCalcModel model,
            NNCalcView view) {

        //Get model info
        NaturalNumber topNN = model.top();
        NaturalNumber bottomNN = model.bottom();
        //Update text areas
        view.updateTopDisplay(topNN);
        view.updateBottomDisplay(bottomNN);
        /*
         * Following code determines which operators should be enabled based on
         * the values of top() and bottom()
         */
        //If bottom <= top
        if (model.bottom().compareTo(model.top()) < 0
                || model.bottom().compareTo(model.top()) == 0) {
            view.updateSubtractAllowed(true);
        } else {
            view.updateSubtractAllowed(false);
        }
        //If bottom > 0
        if (model.bottom().compareTo(new NaturalNumber2()) > 0) {
            view.updateDivideAllowed(true);
        } else {
            view.updateDivideAllowed(false);
        }
        //If bottom <= INT_LIMIT
        if (model.bottom().compareTo(INT_LIMIT) < 0
                || model.bottom().compareTo(INT_LIMIT) == 0) {
            view.updatePowerAllowed(true);
        } else {
            view.updatePowerAllowed(false);
        }
        //If 2 <= bottom <= INT_LIMIT
        if ((model.bottom().compareTo(TWO) > 0
                || model.bottom().compareTo(TWO) == 0)
                && (model.bottom().compareTo(INT_LIMIT) < 0
                        || model.bottom().compareTo(INT_LIMIT) == 0)) {
            view.updateRootAllowed(true);
        } else {
            view.updateRootAllowed(false);
        }

    }

    /**
     * Constructor.
     *
     * @param model
     *            model to connect to
     * @param view
     *            view to connect to
     */
    public NNCalcController1(NNCalcModel model, NNCalcView view) {
        this.model = model;
        this.view = view;
        updateViewToMatchModel(model, view);
    }

    @Override
    public void processClearEvent() {
        /*
         * Get alias to bottom from model
         */
        NaturalNumber bottom = this.model.bottom();
        /*
         * Update model in response to this event
         */
        bottom.clear();
        /*
         * Update view to reflect changes in model
         */
        updateViewToMatchModel(this.model, this.view);
    }

    @Override
    public void processSwapEvent() {
        /*
         * Get aliases to top and bottom from model
         */
        NaturalNumber top = this.model.top();
        NaturalNumber bottom = this.model.bottom();
        /*
         * Update model in response to this event
         */
        NaturalNumber temp = top.newInstance();
        temp.transferFrom(top);
        top.transferFrom(bottom);
        bottom.transferFrom(temp);
        /*
         * Update view to reflect changes in model
         */
        updateViewToMatchModel(this.model, this.view);
    }

    @Override
    public void processEnterEvent() {
        this.model.top().clear();
        //Adds the value of bottom to top
        this.model.top().add(this.model.bottom());
        updateViewToMatchModel(this.model, this.view);
    }

    @Override
    public void processAddEvent() {
        //Adds the value of top to the value of bottom
        this.model.bottom().add(this.model.top());
        this.model.top().clear();

        updateViewToMatchModel(this.model, this.view);

    }

    @Override
    public void processSubtractEvent() {
        //Top value - bottom value
        this.model.top().subtract(this.model.bottom());
        this.model.bottom().transferFrom(this.model.top());

        updateViewToMatchModel(this.model, this.view);

    }

    @Override
    public void processMultiplyEvent() {
        //Top value * bottom value
        this.model.top().multiply(this.model.bottom());
        //Bottom copies the value from top and sets top to be 0
        this.model.bottom().transferFrom(this.model.top());

        updateViewToMatchModel(this.model, this.view);
    }

    @Override
    public void processDivideEvent() {
        //Remainder of top value divided by bottom value
        NaturalNumber rem = this.model.top().divide(this.model.bottom());
        //Bottom copies the value of top and sets top to be 0
        this.model.bottom().transferFrom(this.model.top());
        //Top becomes the value of the remainder
        this.model.top().add(rem);

        updateViewToMatchModel(this.model, this.view);

    }

    @Override
    public void processPowerEvent() {
        //Takes the value of top and raises to the power of bottom's value
        this.model.top().power(this.model.bottom().toInt());
        //Bottom copies the value of top and sets top to be 0
        this.model.bottom().transferFrom(this.model.top());

        updateViewToMatchModel(this.model, this.view);

    }

    @Override
    public void processRootEvent() {
        /*
         * Takes the nth root of the value of top where n is equal to the value
         * of bottom
         */
        this.model.top().root(this.model.bottom().toInt());
        //Bottom copies the value of top and sets top to be 0
        this.model.bottom().transferFrom(this.model.top());

        updateViewToMatchModel(this.model, this.view);

    }

    @Override
    public void processAddNewDigitEvent(int digit) {
        //Adds the digit to the end of bottom
        this.model.bottom().multiplyBy10(digit);
        updateViewToMatchModel(this.model, this.view);

    }

}
