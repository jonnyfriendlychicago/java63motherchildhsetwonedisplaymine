package com.jonfriend.java63motherchildhsetwonedisplaymine.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

//import com.jonfriend.java63motherchildhsetwonedisplaymine.models.TwintwoMdl;
import com.jonfriend.java63motherchildhsetwonedisplaymine.models.OnetwinchildMdl;
import com.jonfriend.java63motherchildhsetwonedisplaymine.models.TwinoneMdl;

@Repository
public interface OnetwinchildRpo extends CrudRepository<OnetwinchildMdl, Long> {
	
	List<OnetwinchildMdl> findAll();
	
	OnetwinchildMdl findByIdIs(Long id);
	
	List<OnetwinchildMdl> findAllByTwinoneMdl(TwinoneMdl twinoneMdl);
//	
//	List<OnetwinchildMdl> findByTwintwoMdlNotContains(TwintwoMdl twintwoMdl);
}
