package groupB.newbankV5.metrics.repositories;

import groupB.newbankV5.metrics.entities.Request;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RequestRepository extends MongoRepository<Request, String> {

}
