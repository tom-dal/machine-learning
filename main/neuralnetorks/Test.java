package neuralnetorks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import neuralnetorks.builder.NetworkBuilder;
import neuralnetorks.enums.ErrorFunctions;
import neuralnetorks.enums.Models;
import neuralnetorks.enums.NetworkOptions;
import neuralnetorks.model.Network;
import neuralnetorks.utils.MathUtilities;

public class Test {
	
	
	

	public static void main(String[] args) {
		
		
	
		
		double[] inputData = {1971, 1972, 1973, 1973, 1974, 1974, 1974, 1975, 1976, 1976, 1976,
		                       1978, 1978, 1979, 1979, 1979, 1981, 1982, 1982, 1983, 1984, 1984,
		                       1985, 1985, 1985, 1986, 1986, 1986, 1987, 1987, 1987, 1987, 1988,
		                       1988, 1989, 1989, 1989, 1989, 1990, 1991, 1991, 1992, 1992, 1993,
		                       1993, 1994, 1994, 1995, 1999, 1995, 1996, 1997, 1997, 1997, 1998,
		                       1999, 1999, 2000, 2000, 1999, 1999, 1999, 2000, 2001, 2001, 2002,
		                       2004, 2004, 2005, 2006, 2005, 2006, 2007, 2008, 2003, 2003, 2002,
		                       2005, 2005, 2006, 2007, 2003, 2008, 2009, 2011, 2012, 2007, 2007,
		                       2008, 2004, 2007, 2008, 2008, 2007, 2009, 2010, 2012, 2013, 2011,
		                       2010, 2010, 2012, 2012, 2010, 2012, 2014, 2006, 2015, 2013, 2015,
		                       2008, 2010, 2014, 2015, 2015, 2012, 2011, 2010, 2014, 2011, 2012,
		                       2014, 2016, 2017, 2017, 2018, 2012, 2016, 2016, 2015, 2013, 2017,
		                       2014, 2017, 2017, 2017, 2012, 2013, 2014, 2017, 2018, 2016, 2018,
		                       2017, 2018, 2017, 2015, 2017, 2016, 2017, 2016, 2017, 2017, 2015,
		                       2017, 2018, 2017, 2017, 2017, 2018, 2018, 2018};
		
		List<Double> inputList = new ArrayList<>();
		
		Arrays.stream(inputData).forEach(d -> inputList.add(d));
		
		double[] targetData = { 2300.,        3500.,        2500.,        2500.,        4100.,
	              4500.,        8000.,        3510.,        5000.,        8500.,
	              6500.,        9000.,       29000.,       17500.,       29000.,
	             68000.,       11500.,       55000.,      134000.,       22000.,
	             63000.,      190000.,      275000.,       25000.,       16000.,
	            110000.,      375000.,       30000.,      385000.,      730000.,
	            273000.,      553000.,      180000.,      250000.,      600000.,
	           1000000.,     1180235.,      310000.,     1200000.,     1350000.,
	             35000.,      600000.,      900000.,     2800000.,     3100000.,
	            578977.,     2500000.,     2500000.,      111000.,     5500000.,
	           4300000.,    10000000.,     7500000.,     8800000.,     7500000.,
	           9500000.,    13500000.,    21000000.,    21000000.,    27400000.,
	          21300000.,    22000000.,    42000000.,   191000000.,    45000000.,
	          55000000.,   112000000.,   400000000.,   169000000.,   184000000.,
	         228000000.,   362000000.,   540000000.,    47000000.,    54300000.,
	         105900000.,   220000000.,   165000000.,   250000000.,   291000000.,
	         169000000.,   410000000.,   600000000.,   760000000.,  1870000000.,
	         432000000.,   463000000.,    26000000.,   230000000.,   592000000.,
	         411000000.,   731000000.,   758000000.,   789000000.,   904000000.,
	        1000000000.,  2990000000.,  1000000000.,  1160000000.,  1170000000.,
	        1200000000.,  1200000000.,  1303000000.,  1400000000.,  1400000000.,
	        1400000000.,  1700000000.,  1750000000.,  1860000000.,  1900000000.,
	        1900000000.,  2000000000.,  2000000000.,  2000000000.,  3000000000.,
	        2100000000.,  2270000000.,  2300000000.,  2600000000.,  2600000000.,
	        2750000000.,  3000000000.,  3000000000.,  5300000000.,  5300000000.,
	        8500000000.,  3100000000.,  3200000000.,  3300000000.,  3990000000.,
	        4200000000.,  4300000000.,  4310000000.,  4800000000.,  4800000000.,
	        4800000000.,  5000000000.,  5000000000.,  5560000000.,  6100000000.,
	        6900000000.,  4000000000.,  6900000000.,  5500000000.,  5500000000.,
	        7000000000.,  7100000000.,  8000000000.,  7200000000.,  8000000000.,
	        8000000000.,  9700000000.,   250000000., 10000000000.,  5450000000.,
	       10000000000.,  4300000000., 18000000000., 19200000000.,  8876000000.,
	       23600000000.,  9000000000. };
		
		
		NetworkBuilder builder = new NetworkBuilder(Models.LINEAR_REGRESSION);
		
		builder.addDeepLayer(5).addDeepLayer(10).addDeepLayer(1).setInputSize(1).setNetworkName("Asghenauei");
		
		Network network = builder.getNetwork();
		
		LearningCore lc = new LearningCore(network).configuration(NetworkOptions.NUMERICAL_DIFFERENTIATION, true);
		lc.setLearningRate(0.001);
		
		
		double[][] inputDataArray = new double[inputData.length][];
		for (int i = 0; i < inputDataArray.length; i++) {
			inputDataArray[i] = MathUtilities.doubleToArray(Math.log(inputData[i]));
		}
		

		lc.configuration(NetworkOptions.INPUT_BATCH_CENTERING, true);
		lc.configuration(NetworkOptions.INPUT_BATCH_NORMALIZATION, true)
//		.configuration(NetworkOptions.TARGET_BATCH_NORMALIZATION, true)
		.configuration(ErrorFunctions.ABSOLUTE_ERROR);
		
		lc.learn(inputDataArray, targetData, 20);
		
//		lc.setLearningRate(0.0001);
//		
//		lc.learn(100);
//		
//		lc.setLearningRate(0.00001);
//		
//		lc.learn(100);
		
		
		double[] test = new double[1];
		
		double result;
		
		
		for (int i = 0; i < 161; i++) {
				test[0] = inputData[i];
				result  = lc.predict(test);
				double target = targetData[i];
				System.out.println("Input: " + test[0] + " Predicted: " + Math.exp(result) + " Actual: " + target + " Error: "
						+ (target - result));
			
		}
		
//		test[0] = 1971;
//		result = lc.predict(test);
//		System.out.println(result);
		

	}

		

}