
public class StringDoublePair {
		public String string;
		public double value;
		
		public StringDoublePair(String string, Double value){
			this.string = string;
			this.value = value.doubleValue();
		}
		
		public String getString() {
			return string;
		}
		
		public double getValue() {
			return value;
		}
}
