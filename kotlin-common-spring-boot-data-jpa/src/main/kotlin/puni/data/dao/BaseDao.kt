package puni.data.dao

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.NoRepositoryBean

/**
 * @author leo
 */
@NoRepositoryBean
interface BaseDao<T, ID> : JpaRepository<T, ID>, JpaSpecificationExecutor<T>
