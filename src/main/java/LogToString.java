import org.apache.commons.exec.LogOutputStream;

public class LogToString extends LogOutputStream {
    private StringBuilder sb = new StringBuilder();

    /**
     * {@inheritDoc}
     *
     * @param logLevel not used here.
     */
    @Override
    public void processLine(String line, int logLevel) {
        sb.append(line);
        sb.append('\n');
    }

    /**
     * Return the log information as string.
     *
     * @return the log information as string.
     */
    @Override
    public String toString(){
        return sb.toString();
    }

    /**
     * Clears the log history stored in this object.
     */
    public void clear() {
        sb.setLength(0);
    }
}