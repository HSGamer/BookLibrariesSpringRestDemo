package me.hsgamer.demodatarest.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        modelMapper.getConfiguration().setFieldMatchingEnabled(true);
        modelMapper.getConfiguration().setSkipNullEnabled(true);

        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);

        String basePackage = "me.hsgamer.demodatarest";
        String modelPackage = basePackage + ".model";
        String responsePackage = basePackage + ".dto.response";

        scanner.addIncludeFilter((metadataReader, metadataReaderFactory) -> true);
        scanner.findCandidateComponents(modelPackage).forEach(beanDefinition -> {
            try {
                Class<?> modelClass = Class.forName(beanDefinition.getBeanClassName());
                Class<?> responseClass = Class.forName(responsePackage + "." + modelClass.getSimpleName() + "Response");
                modelMapper.createTypeMap(modelClass, responseClass);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        return modelMapper;
    }
}
