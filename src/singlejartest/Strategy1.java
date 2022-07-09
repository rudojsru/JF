package singlejartest;

import com.dukascopy.api.*;
import org.apache.commons.lang3.RandomStringUtils;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Strategy1 implements IStrategy {
    private IEngine engine;
    private IConsole console;
    private IHistory history;
    private IContext context;
    private IIndicators indicators;
    private IUserInterface userInterface;

    public void onStart(IContext context) throws JFException {
        this.engine = context.getEngine();
        this.console = context.getConsole();
        this.history = context.getHistory();
        this.context = context;
        this.indicators = context.getIndicators();
        this.userInterface = context.getUserInterface();
    }

    public void onAccount(IAccount account) throws JFException {
    }

    public void onMessage(IMessage message) throws JFException {
    }

    public void onStop() throws JFException {
//        List<IOrder> collect = engine.getOrders().stream().filter(x -> x.getInstrument().equals(Instrument.EURUSD)).collect(Collectors.toList());
//        for (IOrder order : collect) {
//            order.close();
//        }
//        console.getOut().println("Stopped");
    }

    boolean flag = false;
    double price = 0;
    double slPrise = 0;
    double tpPrise = 0;

    public void onTick(Instrument instrument, ITick tick) throws JFException {

        if (flag == false) {
            IOrder order;
            instrument = Instrument.EURUSD;
            tick = history.getLastTick(Instrument.EURUSD);
            price = tick.getAsk() + instrument.getPipValue();
            slPrise = (price + instrument.getPipValue() * 10);
            tpPrise = (price - instrument.getPipValue() * 10);
            String generatedString = RandomStringUtils.randomAlphabetic(10);

            console.getOut().println("........................Order   was created  SELL.................");
            System.out.println("Prise: " + price + ", stopLoss: " + slPrise + ", takeProfit:" + tpPrise);
            order = engine.submitOrder(generatedString, instrument, IEngine.OrderCommand.SELL, 0.001, 0, 40, slPrise, tpPrise);//,price,10,(price+0.03),tpPrise);

            order.waitForUpdate(2000);
            console.getOut().println("S...............................................................S");
            flag = true;

            //order = engine.submitOrder("hhhh", instrument, IEngine.OrderCommand.SELL, 0.001,0,40,1.0865,1.0830);//,price,10,(price+0.03),tpPrise);
        }

        // con.getOut().println(instr+" "+tick.getAsk()+"/"+tick.getBid());
    }

    public void onBar(Instrument instrument, Period period, IBar askBar, IBar bidBar) throws JFException {

        counterOfOrders();

    }

    private void counterOfOrders() throws JFException {
        Date d1 = new Date();
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/YYYY HH:mm a");
        String formattedDate = df.format(d1);
        int counter=0;
        System.out.println( formattedDate);
        System.out.println( engine.getOrders().toString());
        for (IOrder order : engine.getOrders())
            console.getOut().println(++counter +") " + order.getAmount() + "  " + order.getInstrument() + "  "+order.getCreationTime());
        System.out.println("counterOfOrders--------------------counterOfOrders");
    }

}