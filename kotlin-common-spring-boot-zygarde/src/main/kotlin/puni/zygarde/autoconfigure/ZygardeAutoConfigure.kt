package puni.zygarde.autoconfigure

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EnableJpaRepositories("puni.zygarde.data.dao")
@ComponentScan("puni.zygarde")
class ZygardeAutoConfigure
