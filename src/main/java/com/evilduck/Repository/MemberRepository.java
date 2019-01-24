package com.evilduck.Repository;

import com.evilduck.Entity.MemberEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MemberRepository extends MongoRepository<MemberEntity, String> {

    MemberEntity findByName(final String name);

}
