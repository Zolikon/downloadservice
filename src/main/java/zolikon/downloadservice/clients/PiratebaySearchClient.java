package zolikon.downloadservice.clients;


import com.google.inject.Singleton;
import jpa.Jpa;
import jpa.Query;
import jpa.Torrent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Singleton
public class PiratebaySearchClient {

    private static final List<Predicate<Torrent>> FILTER_LIST;

    static {
        FILTER_LIST = new ArrayList<>();
        FILTER_LIST.add(t -> t.Name.contains("1080"));
        FILTER_LIST.add(t -> t.Name.contains("720"));
        FILTER_LIST.add(t -> t.Name.contains("ettv"));
        FILTER_LIST.add(t -> true);
    }

    public Optional<Torrent> search(String name) {
        Query query = new Query(name, 0);
        List<Torrent> list = Jpa.Search(query);
        Optional<Torrent> optionalTorrent = getTorrent(list);
        return optionalTorrent;
    }

    private static Optional<Torrent> getTorrent(List<Torrent> torrentList) {
        Optional<Torrent> optionalTorrent = Optional.empty();
        try {
            if (torrentList.size() > 0) {
                Iterator<Predicate<Torrent>> it = FILTER_LIST.iterator();
                while (!optionalTorrent.isPresent() && it.hasNext()) {
                    Predicate<Torrent> next = it.next();
                    optionalTorrent = torrentList.stream()
                            .filter(t -> t.Seeds > 0)
                            .filter(next)
                            .sorted((one, two) -> two.Seeds - one.Seeds)
                            .findFirst();
                }
            }
        } catch (Exception exc) {
            //TODO add logging
        }
        return optionalTorrent;
    }

}
