package io.shike.gremzeppelin;

import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.tinkerpop.gremlin.driver.Client;
import org.apache.tinkerpop.gremlin.driver.Cluster;
import org.apache.tinkerpop.gremlin.driver.ResultSet;
import org.apache.zeppelin.interpreter.Interpreter;
import org.apache.zeppelin.interpreter.InterpreterContext;
import org.apache.zeppelin.interpreter.InterpreterResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Properties;

/**
 * @author Ranger Tsao(https://github.com/boliza)
 */
public class GremlinInterpreter extends Interpreter {

    private static final Logger logger = LoggerFactory.getLogger(GremlinInterpreter.class);

    private static final String DEFAULT_GREMLIN_SERVER_HOSTS = "127.0.0.1";
    private static final int DEFAULT_GREMLIN_SERVER_PORT = 8182;

    private String GREMLIN_SERVER_HOSTS = "gremlin.server.hosts";
    private String GREMLIN_SERVER_PORT = "gremlin.server.port";

    private Client client;
    private Cluster cluster;

    public GremlinInterpreter(Properties property) {
        super(property);
    }

    @Override
    public void open() {
        logger.info("init gremlin client via {}", property);
        Configuration configuration = new BaseConfiguration();
        property.keySet().forEach(key -> configuration.setProperty(key.toString(), property.get(key)));
        //transform hosts
        String hosts = configuration.getString(GREMLIN_SERVER_HOSTS, DEFAULT_GREMLIN_SERVER_HOSTS);
        configuration.clearProperty(GREMLIN_SERVER_HOSTS);
        configuration.setProperty(GREMLIN_SERVER_HOSTS, Arrays.asList(hosts.split(",")));

        cluster = Cluster.open(configuration.subset("gremlin.server"));
        client = cluster.connect();
    }

    @Override
    public void close() {
        client.close();
        cluster.close();
    }

    @Override
    public InterpreterResult interpret(String gremlin, InterpreterContext interpreterContext) {
        logger.info("execute gremlin traversal {}", gremlin);
        try {
            ResultSet results = client.submit(gremlin);
            JsonArray array = results.stream()
                                     .map(result -> new JsonPrimitive(results.toString()))
                                     .collect(JsonArray::new, JsonArray::add, JsonArray::addAll);

            //TODO extract ResultSet
            //Case MessageSerializer

            return new InterpreterResult(InterpreterResult.Code.SUCCESS, array.toString());
        } catch (RuntimeException e) {
            return new InterpreterResult(InterpreterResult.Code.ERROR, e.getMessage());
        }
    }

    @Override
    public void cancel(InterpreterContext interpreterContext) {
        //do nothing
    }

    @Override
    public FormType getFormType() {
        return FormType.SIMPLE;
    }

    @Override
    public int getProgress(InterpreterContext interpreterContext) {
        return 0;
    }

}
