package io.shike.gremzeppelin;

import org.apache.zeppelin.interpreter.Interpreter;
import org.apache.zeppelin.interpreter.InterpreterContext;
import org.apache.zeppelin.interpreter.InterpreterResult;

import java.util.Properties;

/**
 * @author Ranger Tsao(https://github.com/boliza)
 */
public class GremlinInterpreter extends Interpreter {

    public GremlinInterpreter(Properties property) {
        super(property);
    }

    @Override
    public void open() {

    }

    @Override
    public void close() {

    }

    @Override
    public InterpreterResult interpret(String s, InterpreterContext interpreterContext) {
        return null;
    }

    @Override
    public void cancel(InterpreterContext interpreterContext) {

    }

    @Override
    public FormType getFormType() {
        return null;
    }

    @Override
    public int getProgress(InterpreterContext interpreterContext) {
        return 0;
    }
}
