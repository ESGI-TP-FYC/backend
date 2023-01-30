package fr.esgi.ecommerce.service.graphql;

import fr.esgi.ecommerce.service.OrderService;
import fr.esgi.ecommerce.service.ProductService;
import fr.esgi.ecommerce.service.UserService;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Component
@RequiredArgsConstructor
public class GraphQLProvider {

    private final ProductService productService;
    private final OrderService orderService;
    private final UserService userService;

    @Value("classpath:graphql/schemas.graphql")
    private Resource resource;

    @Getter
    private GraphQL graphQL;

    @PostConstruct
    public void loadSchema() throws IOException {
        InputStream fileSchema = resource.getInputStream();
        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(new InputStreamReader(fileSchema));
        RuntimeWiring wiring = buildRuntimeWiring();
        GraphQLSchema schema = new SchemaGenerator().makeExecutableSchema(typeRegistry, wiring);
        graphQL = GraphQL.newGraphQL(schema).build();
    }

    private RuntimeWiring buildRuntimeWiring() {
        return RuntimeWiring.newRuntimeWiring()
                .type("Query", typeWiring -> typeWiring
                        .dataFetcher("products", productService.getAllProductsByQuery())
                        .dataFetcher("productsIds", productService.getAllProductsByIdsQuery())
                        .dataFetcher("product", productService.getProductByQuery())
                        .dataFetcher("orders", orderService.getAllOrdersByQuery())
                        .dataFetcher("ordersByEmail", orderService.getUserOrdersByEmailQuery())
                        .dataFetcher("users", userService.getAllUsersByQuery())
                        .dataFetcher("user", userService.getUserByQuery()))
                .build();
    }
}
