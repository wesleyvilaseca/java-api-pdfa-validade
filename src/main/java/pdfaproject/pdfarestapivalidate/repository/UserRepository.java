package pdfaproject.pdfarestapivalidate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pdfaproject.pdfarestapivalidate.model.UserModel;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {
}
