package Server.DAO;

import Server.Entity.Humanplayer;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import Server.DAO.HibernateUtil;

import java.util.List;

public class HumanPlayerDAO {
    public static List<Humanplayer> getAllplayer(){
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Humanplayer> humanplayerList = null;
        try{
            final String hql = "select st from Human player st";
            Query query = session.createQuery(hql);
            humanplayerList = query.list();

        }catch (HibernateException e){
            System.err.println(e);
        }
        return humanplayerList;
    }
}
