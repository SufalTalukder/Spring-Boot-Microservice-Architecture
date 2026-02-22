package com.sufaltalukder.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sufaltalukder.Models.AuthPermissionModel;
import com.sufaltalukder.Models.AuthUserModel;

@Repository
public interface AuthPermissionRepository extends JpaRepository<AuthPermissionModel, Long> {

	Optional<AuthPermissionModel> findByAuthUserInfo(AuthUserModel updatedUser);

	@Query("""
				SELECT pm
				FROM AuthPermissionModel pm
				LEFT JOIN FETCH pm.authUserInfo
				ORDER BY pm.authPermissionCreatedAt DESC
			""")
	List<AuthPermissionModel> findAllPermissions();

	Optional<AuthPermissionModel> findByAuthPermissionId(long authPermissionId);

}
