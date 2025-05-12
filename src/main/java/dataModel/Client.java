package dataModel;

/**
 * Represents a client entity with personal information, such as name, email, address, age and ID.
 */

public class Client {

    private String name;
    private String email;
    private String address;
    private int age;
    private int id;

    /**
     * Default constructor.
     */

    public Client() {

    }

    /**
     * Constructs a Client with all fields including ID.
     * @param id the client's unique identifier
     * @param name the client's name
     * @param address the client's address
     * @param email the email of the client
     * @param age the client's age
     */

    public Client(int id, String name, String address, String email, int age) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.email = email;
        this.age = age;
    }

    /**
     * Constructs a Client without an ID.
     * @param name the client's name
     * @param address the client's address
     * @param email the email of the client
     * @param age the client's age
     */

    public Client(String name, String address, String email, int age) {
        this.name = name;
        this.address = address;
        this.email = email;
        this.age = age;
    }

    /**
     * Gets the name of the client.
     *
     * @return the client name
     */

    public String getName() {
        return name;
    }

    /**
     * Sets the name of the client.
     *
     * @param name the client name
     */

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the email of the client.
     *
     * @return the client email
     */

    public String getEmail() {
        return email;
    }

    /**
     * Sets the email of the client.
     *
     * @param email the client email
     */

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the age of the client.
     *
     * @return the client age
     */

    public int getAge() {
        return age;
    }

    /**
     * Sets the age of the client.
     *
     * @param age the client age
     */

    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Gets the ID of the client.
     *
     * @return the client ID
     */

    public int getId() {
        return id;
    }

    /**
     * Sets the ID of the client.
     *
     * @param id the client ID
     */

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the address of the client.
     *
     * @return the client's address
     */

    public String getAddress() {
        return address;
    }

    /**
     * Sets the address of the client.
     *
     * @param address the client's address
     */

    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Returns a summary string of the client.
     * @return a formatted string with basic client information
     */

    @Override
    public String toString() {
        return String.format("Client[id=%d, name=%s, email=%s, address=%s, age=%d]\n", id, name, email, address, age);
    }
}
