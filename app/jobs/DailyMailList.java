package jobs;

import cache.CacheUnit;
import cache.ItemCache;
import models.Item;
import models.Subscription;
import org.apache.commons.mail.EmailException;
import play.db.jpa.JPAPlugin;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * User: eguller
 * Date: 4/6/14
 * Time: 11:25 PM
 */
public class DailyMailList extends EmailList{
    private static final int ITEM_SIZE = 30;
    int today;
    public DailyMailList(){
        today =  Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
    }
    @Override
    public void send() {
        List<Subscription> subscriptionList = Subscription.dailySubscribers(today);
        List<Item> itemList = getItems();
        if(!(itemList.size() < ItemCache.ITEM_PER_PAGE)) {
            sendEmail(subscriptionList, itemList, subject());
        }
    }

    @Override
    public String subject() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        DateFormat yesterday = new SimpleDateFormat("EEEE, dd MMMM");
        String timePrefix =  yesterday.format(calendar.getTime());
        return timePrefix + " - Daily Top Links";
    }

    @Override
    public int postCount() {
        return ITEM_SIZE;
    }

    @Override
    public CacheUnit cacheUnit() {
        return CacheUnit.today;
    }

    @Override
    public void sendEmail(List<Subscription> subscriptions, List<Item> itemList, String subject) {
        for(Subscription subscription : subscriptions){
            try {
                sendEmail(subscription, itemList, subject);
                JPAPlugin.startTx(false);
                subscription.setDay(today);
                subscription.save();
                JPAPlugin.closeTx(false);
            } catch (EmailException e) {
                e.printStackTrace();
            }
        }
    }
}
