package Server.Entity;
import Server.DAO.HumanPlayerDAO;
import Server.Entity.Humanplayer;
import java.util.List;
public class Main {
    public static void main(String[] args) {
        List<Humanplayer> rs = HumanPlayerDAO.getAllplayer();
        for (Humanplayer item: rs){
            System.out.println(item.getUsername());
        }
        }
}
