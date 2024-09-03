package Administrator.Algorithm;

import java.util.ArrayList;

public class DeepQNetworkController_old {

	private Input input;
	private Output output;
	private Layer1 layer1;
	private Layer2 layer2;
	private float[] actions;
	
	private float dt = 30; // 0.03 s in ms dt used to train the cyberplayer
	
	
	public DeepQNetworkController_old() {
		actions = new float[] {-4f, -2f, -1f, -0.5f, 0, 0.5f, 1f, 2f, 4f};
		input = new Input();
		output = new Output();
		layer1 = new Layer1();
		layer2 = new Layer2();
	}
	

	private class Input {
		
		double[] offset = new double[] {0.0807, 0.1995, 0.0698, 0.1230};
		double[] gain = new double[] {3.2883, 3.1833, 2.5057, 4.5601};
		int ymin = -1;
	}
	
	private class Output {
		
		double[] offset = new double[] {0.30908, 0.053588, 0.0010657, 0.018846, 0.067084, 0.075833, 0.29768, 0.061345, 0.38352};
		double[] gain = new double[] {6.1222, 3.9671, 2.8495, 3.9499, 2.1576, 3.6714, 4.2301, 2.8902, 4.5847};
		int ymin = -1;
	}
	
	private class Layer1 {
		
		// b1: 64x1
		double[] b = new double[] {-3.5905, 3.9303, 3.4945, -3.6251, 3.5872, -3.3324, -3.2052, 2.0564, 3.0727, 3.1541, 
				2.7842, 3.4231, 2.7489, 2.4644, -2.3067, 1.6131, -2.0674, -1.8228, -0.63343, 1.4655, 
				1.489, -1.3453, 0.97619, -0.61068, -1.5359, 1.0686, 1.1355, -0.73268, -0.3132, -0.20139, 
				0.12433, 0.84865, 0.24568, 0.32438, 0.57956, -0.77844, -0.016834, -0.6864, -1.0787, -0.5255, 
				1.0885, 1.7874, -1.4738, -1.4117, 1.187, 1.5663, 1.7626, -2.3, 2.2476, -2.0044, 
				-2.2103, 4.2199, 1.8594, -3.2595, 2.849, 3.1965, -3.0052, 3.3451, 3.6101, 3.7313, 
				-3.3801, -3.3541, -4.1376, -4.0616};
		// weight: 64x4
		double[][] weight = new double[][] {{0.15738, 0.82047, 2.1622, -3.4124},{-1.2436, -0.53736, -1.6591, -3.1653},
			{-1.0893, -2.8491, 1.3135, 2.2642},{1.6502, 2.1966, 1.5595, 2.3332},{-0.92823, -3.5516, -1.0031, -0.48915},
			{2.2316, 2.5796, 1.8984, 0.65912},{0.82374, 3.21, 1.9968, 0.84422},{0.72236, 3.709, -0.21083, 2.2933},
			{-1.5232, 1.4061, -2.2664, -2.1941},{-1.204, -1.0854, 2.1204, -2.4492},{-1.1735, -3.141, 0.85252, -1.8479},
			{-0.61818, 0.28245, 3.3249, 0.38581},{-2.8948, -0.46009, 1.2874, -1.7485},{-1.7285, -2.2478, 0.85611, 2.1939},
			{2.1595, 0.62571, 3.0957, -0.64441},{-1.5421, -2.03, 2.8181, 1.2914},{2.8247, 2.5465, -0.70215, 0.69319},
			{2.9245, 0.79786, 2.4966, 0.50817},{0.29499, -3.5382, -0.17978, -2.0676},{-0.5862, -0.321, 1.5116, 3.4207},
			{-1.5293, 2.9979, -0.21777, -1.7039},{1.5435, -0.58696, 3.4514, 0.94044},{-0.32516, 0.10926, 2.9371, 2.3585},
			{2.1271, 2.2282, -1.9476, -1.5401},{2.6582, 1.8501, -1.3973, 1.0236},{-2.7396, 0.52131, 0.90135, -2.2955},
			{-1.5102, -1.9941, 2.5138, 1.1657},{-0.59736, 2.9387, -1.458, -1.5686},{2.8735, 0.070937, -2.4693, -0.38689},
			{1.0667, -3.2672, 1.3995, 0.84147},{-2.0985, 1.6528, 2.0313, -1.6808},{-2.6285, -2.6436, 0.26675, -1.1891},
			{-1.2674, -0.72601, -2.4075, 2.5728},{1.9202, 1.6352, 1.686, -2.1464},{0.99138, -1.3686, -2.3988, -2.5167},
			{-2.2063, 1.6828, 1.7606, 1.8129},{1.4143, -0.48669, -3.5697, -0.32546},{-1.6552, -1.0756, 1.6276, 2.75},
			{-2.4733, 1.2068, 2.5136, 0.97989},{-2.5666, -2.8999, -0.80558, -1.1341},{2.1509, -1.2975, 2.6115, -0.64719},
			{1.872, -2.7253, 0.049896, -1.5371},{-1.0469, 2.7343, -1.9678, -1.3845},{-1.2936, -2.6751, -1.3394, 1.7093},
			{2.1183, 3.1666, -0.070839, -0.44437},{1.0237, -2.8668, 1.8415, 1.402},{2.5908, 1.688, -0.91968, -2.0767},
			{-2.2979, 0.65862, 2.6926, 0.82193},{2.3148, -1.9759, -0.13016, -2.1501},{-2.0597, -1.1486, -3.2076, -0.80759},
			{-0.16758, -2.8826, 1.1897, 2.0465},{0.042608, -0.26123, 2.4171, 0.16196},{1.9921, 3.6016, 0.82559, -0.27145},
			{-1.8337, 2.6319, 1.0497, 1.0116},{1.8864, -2.939, -0.64047, 1.4164},{2.1444, 0.29451, -2.0152, -1.9312},
			{-1.0962, -3.372, 1.6358, 0.14606},{-0.0007747, 0.93684, -2.4293, -2.6884},{1.9074, -1.4711, 2.1114, 1.4117},
			{-0.10686, 1.1159, 3.4216, -0.30465},{-2.1255, -2.8882, -0.95354, -1.597},{-1.3577, -2.8305, 1.3779, 2.2689},
			{-1.9269, 2.4753, 1.3554, 1.3626},{-1.4325, -0.11273, -2.4534, -2.3835}};
	}
	
	private class Layer2 {
		
		// b2: 9x1
		double[] b = new double[] {-0.096301, 0.018623, -1.3813, -1.3855, 0.47052, 0.14576, -0.31057, -0.40415, -1.2497};
		// weight: 9x64
		double[][] weight = new double[][] {{0.079681, -0.64914, -0.02345, 0.60386, -1.3134, 0.12346, 0.43123, -0.28706, 0.29293, 0.71128, 
			-1.4891, -0.6204, -0.85006, 0.64611, -0.49145, -0.63201, 1.4947, 0.82673, -0.36858, -0.29209, 
			-0.12141, -1.3276, -0.40694, 0.46322, 0.031983, -0.19638, 1.0052, 0.39966, -0.43648, -0.30826, 
			0.3705, -0.56589, 0.1947, 0.73506, 0.25223, -1.0643, 0.69856, 0.497, -0.62834, -0.59694, 
			-0.0017778, -0.77116, -0.19214, 0.58712, 0.57967, 0.59335, -0.33227, 0.0243, 0.80341, -0.45031, 
			-0.29596, 1.7335, 0.59107, 1.0051, -0.23525, -0.74994, -0.30713, 0.10947, -0.10974, -0.68015, 
			0.14841, -0.10913, 0.20391, -0.17431},
			{0.23496, 0.085792, 0.2275, 1.3424, 0.093019, 0.37229, 0.14344, 0.033845, -0.10496, -0.10024, 
			0.38863, -0.66852, 0.16751, 0.28171, 0.28288, 0.24974, 0.90166, -0.04172, -0.64602, 0.042922, 
			0.65739, -1.1489, -0.42502, 0.59429, 0.27583, 0.011465, 0.12126, -0.7859, -0.45176, -0.29597, 
			0.10944, -0.82221, 0.28751, 0.15459, 0.052067, 0.23147, 1.0983, 0.10886, 0.16559, -0.68856, 
			-0.059995, 0.57848, 0.66829, -0.21258, -0.017442, 0.41478, 0.31872, -0.56445, 0.24335, -0.77651, 
			0.75048, 1.233, 0.78208, 0.53882, -0.17988, 0.2203, 0.010791, -0.39978, 0.31337, -0.44352, 
			0.5857, -0.1985, 0.99205, 0.11684},
			{-0.077226, -0.043045, -0.3074, 0.21064, -0.73944, -0.13034, 1.1884, -0.16174, -0.016899, -0.045668, 
			-0.32037, -0.51963, 0.093481, 0.53744, -0.0076974, -0.16729, -0.28725, -0.40414, -0.32342, -0.075823, 
			-0.039757, -0.3304, -0.1964, 0.25298, 0.34939, -0.030295, 0.35437, 0.017162, -0.2663, -0.10067, 
			0.16515, 0.056826, 0.045906, 0.44906, 0.29291, -0.43403, 0.59013, 0.033743, -0.68324, -0.79162, 
			-0.027274, -0.06754, -0.0096669, 0.2018, -0.1858, 0.12252, -0.04421, 0.11368, -0.055269, 0.40768, 
			-0.35333, 1.0543, 0.68655, 0.84559, -0.030791, -0.22287, -0.13078, 0.1447, 0.13568, -0.28447, 
			0.10348, 0.28844, 0.035843, -0.022696},
			{-0.24932, -0.24197, -0.10275, 0.46309, 0.22087, 0.99056, -0.18114, 0.1769, 0.33564, 0.47756,
			-1.1505, -0.4716, -0.95871, 0.43668, 0.29654, -0.063188, -0.33139, 1.0288, -0.21333, -0.3095,
			0.01564, -0.57529, -0.74041, -0.003483, -0.080691, -0.14897, -0.067247, 0.46339, -0.39992, 0.041244,
			-0.00056092, -0.92918, -0.16629, 0.636, 0.51679, -0.34436, 1.0945, 0.06289, 0.088226, 0.088434,
			-0.15951, -0.1279, -0.21005, 0.27447, 0.24037, 0.21332, -0.11982, -0.92688, 0.56917, -0.64502,
			-0.49604, 1.6511, 0.49345, 1.2221, -0.14491, -0.35496, -0.22337, -0.49169, -0.0022522, -0.68258,
			0.53711, 0.4619, -0.58217, -0.23418},
			{-0.0093362, -0.35025, -0.040853, 1.0066, 0.39769, -0.60044, 0.85679, -0.04642, -0.062429, 0.022322, 
			-1.2978, -0.43139, -0.03521, 0.14285, 0.16209, 0.69771, 1.2153, 1.1587, -0.56275, 0.12115, 
			-0.040861, -0.81854, -0.11979, -0.042478, -0.086777, -0.024038, -0.83948, 0.33845, -0.21765, 0.10928, 
			0.075571, 0.20133, 0.028026, 0.11827, 0.47021, -0.12801, 0.45606, -0.10869, 0.074271, -0.30501,  
			-0.011353, 0.35585, -0.14327, 0.023508, 0.067084, 0.081317, -0.11133, -0.40456, 0.025087, -0.21065,  
			-0.22494, 0.76494, 0.29472, -0.47373, -0.034812, -0.0057648, -0.046974, -0.015232, 0.08095, -0.20218,  
			0.3199, 0.15588, 0.33633, 0.086419},
			{-0.30951, 0.13568, 0.44913, -0.41692, -0.81662, 1.0974, 0.76858, -0.55718, 0.28865, -0.22645, 
			0.057329, -0.62481, 0.29542, -0.3866, 0.010447, 0.3702, 0.40488, 1.1451, -0.45788, -0.004195, 
			-0.13978, -0.57094, -0.55284, -0.088886, 0.13949, -0.011252, -0.40148, -0.62665, -0.32156, 0.21331, 
			0.23314, -0.88756, -0.26621, -0.026894, -0.12186, -0.65383, 0.94461, 0.29824, -0.60051, -1.0982, 
			-0.025947, -0.44145, -0.18929, -0.027336, 0.22526, -0.82699, 0.10317, -0.06448, 0.34188, -0.21297, 
			-0.2357, 1.3868, 0.35193, -0.079978, -0.1795, -0.015615, -0.20464, -0.63063, -0.088688, -0.51641, 
			-0.24711, 0.082447, 0.31415, 0.095203},
			{-0.2994, 0.4053, 0.18219, 0.98382, -0.24413, 0.92559, -0.02057, 0.38999, -0.30751, 0.22775, 
			-0.26068, -0.79815, -0.086983, 0.038242, 0.46743, 0.50105, 0.70771, 0.078402, 0.32621, -0.3811,
			-0.16236, -0.58194, -0.72848, 0.1734, -0.064306, -0.18844, -0.50716, -0.18958, -0.46044, -0.054189, 
			0.32304, -1.0447, -0.26393, 0.33729, 0.071523, 0.31864, 1.2688, 0.16401, -0.96981, -0.89832, 
			-0.026764, -0.3507, 0.097229, 0.2661, 0.12826, -0.040008, -0.10496, 0.018214, 0.47902, -0.37335, 
			-0.56443, 1.118, 0.41334, -0.23154, -0.11656, -0.79848, -0.045129, 0.51493, 0.22426, -0.3202, 
			0.030692, 0.44371, 1.5428, -0.15998},
			{0.065302, -0.22971, 0.020374, 0.043609, -0.20848, 1.0733, 0.29845, 0.15368, -0.09401, 0.2747, 
			-0.57132, -0.57739, -0.30687, 0.13072, 0.0038261, -0.57565, 0.5091, -0.0020377, 0.33294, 0.14121, 
			0.21132, -0.31254, -0.48842, 0.26602, 0.11983, -0.051352, 0.67633, -0.81255, -0.37115, 0.076861, 
			0.13049, -0.40212, 0.1536, 0.21788, 0.7508, 0.63714, 0.71764, -0.1098, 0.11993, 0.23991, 
			-0.062483, -0.15557, 0.15954, 0.075872, 0.12908, -0.54035, 0.21301, -0.76164, 0.42923, 0.46707, 
			0.25458, 0.91401, 0.35306, 1.1654, 0.022261, 0.093675, -0.11191, -0.43064, 0.21956, -0.27309, 
			0.053691, 0.0055421, 0.68622, 0.17581},
			{0.073635, -0.59084, -0.11661, -0.0012997, 0.016739, 1.0487, 0.27119, 0.082336, 0.063498, 0.23905, 
			-0.44472, -1.2479, 0.34261, 0.47648, 1.0163, -0.45625, 0.79664, 0.71451, 0.41993, 0.16206, 
			0.0067086, -0.41664, -0.20754, 0.13103, 0.35756, -0.090446, 0.60119, -0.49591, -0.75536, 0.26348, 
			0.2073, -0.49756, 0.0097603, 0.11544, 0.53757, -0.71088, 0.54305, -0.23919, -0.23598, -0.70169, 
			-0.00062142, -0.26344, -0.0014406, -0.24936, 0.92492, -0.52632, 0.022506, -0.92029, -0.17487, 
			0.22332, -0.52709, 1.0155, -0.19185, 0.018338, 0.089004, 0.14772, -0.27668, -0.23356, 0.51915, 
			-0.23032, 0.046846, 0.45582, 1.5269, 0.11084}};
	}
	
	
	private double[] mapminmax_apply(double[] x, Input input) {
		// 4 elements
		double y[] = new double[x.length];
		for(int i = 0; i < x.length; i++) {
			y[i] = x[i] - input.offset[i];
			y[i] = y[i] * input.gain[i];
			y[i] = y[i] + input.ymin;
		}
		
		return y;
	}
	
	private double[] evaluate_layer1(Layer1 layer, double[] x) {
		// return 64 elements
		double[] product = productRowsColumn(layer.weight, x);
		
		double n[] = new double[product.length];
		double a[] = new double[product.length];
		for(int i = 0; i < product.length; i++) {
			n[i] = layer.b[i] + product[i];
			a[i] = 2/(1 + (float)Math.exp(-2*n[i])) - 1;
		}
		
		return a;
	}
	
	private double[] evaluate_layer2(Layer2 layer, double[] x) {
		// return 64 elements
		double[] product = productRowsColumn(layer.weight, x);
		
		double a[] = new double[product.length];
		for(int i = 0; i < product.length; i++) {
			a[i] = layer.b[i] + product[i];
		}
		
		return a;
	}
	
	private double[] productRowsColumn(double[][] a, double[] b) {
		
		double result[] = new double[a.length];
		
		// loop over rows of a
		for(int i=0; i < a.length; i++) {
			double rowSum = 0;
			for(int j=0; j < b.length; j++) {
				rowSum = rowSum + (a[i][j] * b[j]);
			}
			result[i] = rowSum;
		}
		
		return result;
	}
	
	private double[] mapminmax_reverse(double[] y, Output output) {
		// 9 elements
		double x[] = new double[y.length];
		for(int i = 0; i < y.length; i++) {
			x[i] = y[i] - output.ymin;
			x[i] = x[i] / output.gain[i];
			x[i] = x[i] + output.offset[i];
		}
		
		return x;
	}
	
	
	public float controllerLawImproviser(float x, float y, float Rp, float Sv) {
		
		System.out.println("DEBUG - state: " + x + ", " + y + ", " + Rp + ", " + Sv);
		
		// extended state: pos leader, vel leader, err pos, err vel
		double[] currentExtendedState = new double[] {Rp, Sv, x-Rp, y-Sv};
				
		// evaluate NN
		double[] xp1 = mapminmax_apply(currentExtendedState, input);
		double[] a1 = evaluate_layer1(layer1, xp1);
		double[] a2 = evaluate_layer2(layer2, a1);
		double[] y1 = mapminmax_reverse(a2, output);
		
		float u = 0;
		int maxIndex = 0;
        for(int i = 1; i < y1.length; i++) {
        	if(y1[i] > y1[maxIndex]) {
        		maxIndex = i;
        	}
        }
        
        u = actions[maxIndex];		
		return u;
	}
	
	public float getDt() {
		return dt;
	}
	
}
