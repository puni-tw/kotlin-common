package puni.data.dao

import org.springframework.data.repository.NoRepositoryBean

/**
 * @author leo
 */
@NoRepositoryBean
interface AutoIdDao<T> : BaseDao<T, Long>
