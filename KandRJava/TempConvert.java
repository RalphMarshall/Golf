class TempConvert {

    /**
     * Program to print out a Fahrenheit to Celcius conversion table
     *
     * Output to look like the following:
     *   0  -17.8
     *  20   -6.7
     *  40    4.4
     * ...  .....
     * 260  126.7
     * 280  137.8
     * 300  148.9
     *
     * For historical interest, here is the code from the original K&R book
     *
     * main()
     * {
     *     int lower, upper, step;
     *     float fahr, celcius;
     *     
     *     lower = 0;
     *     upper = 300;
     *     step = 20;
     *
     *     fahr = lower;
     *     while (fahr <= upper) {
     *         celcius = (5.0/9.0) * (fahr - 32.0);
     *         printf("%4.0f %6.1f\n", fahr, celcius);
     *         fahr = fahr + step;
     *     }
     * }
     */
    
    public static void main(String [] args) {

	for (double f = 0; f <= 300; f += 20) {
	    System.out.printf("%4.0f  %6.1f\n", f, fToC(f));
	}
    }

    private static double fToC(double f) {
	return (f-32.0) * (5.0/9.0);
    }
}
