package url.shorten.service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import url.shorten.service.model.Url;

import java.util.Optional;

@Repository
public interface URLRepository extends JpaRepository<Url, Long> {
    Optional<Url> findByShortUrl(String shortUrl);

    Optional<Url> findByLongUrl(String shortUrl);
}
