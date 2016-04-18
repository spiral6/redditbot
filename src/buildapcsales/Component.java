package buildapcsales;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Component {

	private String model;
	private String price;
	private String manufacturer;
	
	public Component(){}
	
	public Component(String manufacturer, String model, String price) throws FileNotFoundException{
		this.manufacturer = manufacturer;
		this.model = model;
		aliases();
		this.price = price;
	}
	
    public String getprice() {
        return price;
    }

    public void setprice(String price) {
        this.price = price;
    }
    
    public String getmodel() {
        return model;
    }

    public void setmodel(String model) throws FileNotFoundException {
        this.model = model;
        aliases();
    }
    
    public String getmanufacturer() {
        return manufacturer;
    }

    public void setmanufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }
    
    public void aliases() throws FileNotFoundException{
		File alias = new File("src/buildapcsales/alias.txt");
		model.trim();
		Scanner fs = new Scanner(alias);
		while(fs.hasNextLine()){
			String line = fs.nextLine();
			if(line.contains(model)){
				model = line.substring(0, line.indexOf(":"));
			}
		}
	}
    
	public String toString(){
		return "Manufacturer: " + manufacturer + "\n" + "Model: " + model + "\n" + "Price: " + price; 
	}

}
