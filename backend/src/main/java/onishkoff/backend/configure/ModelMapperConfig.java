package onishkoff.backend.configure;

import lombok.RequiredArgsConstructor;
import onishkoff.backend.dto.auth.RegistrationDto;
import onishkoff.backend.model.User;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ModelMapperConfig {

    private final PasswordEncoder passwordEncoder;

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.typeMap(RegistrationDto.class, User.class).addMappings(mapper -> {
            mapper.using((MappingContext<String, String> context) ->
                            passwordEncoder.encode(context.getSource()))
                    .map(RegistrationDto::getPassword, User::setPassword);
        });
        return modelMapper;

    }
}
