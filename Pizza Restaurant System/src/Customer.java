public class Customer {
    private Integer id;
    private String name;
    private String surname;
    private String number;
    private String address;

    public Customer(Integer id, String surname, String name, String number, String address) {
        this.id = id;
        this.name = surname;
        this.surname = name;
        this.number = number;
        this.address = address;
    }

    public Integer getId() {
        return id;
    }

    public String getSurname() {
        return surname;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

}

