package ma.fzl.ebankingbackend.repositories;

import ma.fzl.ebankingbackend.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository <Customer , Long> {
    List<Customer> findByNameContains(String keyword);
}
