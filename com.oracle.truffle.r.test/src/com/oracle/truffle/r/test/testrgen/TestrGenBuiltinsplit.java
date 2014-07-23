/*
 * This material is distributed under the GNU General Public License
 * Version 2. You may review the terms of this license at
 * http://www.gnu.org/licenses/gpl-2.0.html
 * 
 * Copyright (c) 2014, Purdue University
 * Copyright (c) 2014, Oracle and/or its affiliates
 * All rights reserved.
 */
package com.oracle.truffle.r.test.testrgen;

import org.junit.*;

import com.oracle.truffle.r.test.*;

public class TestrGenBuiltinsplit extends TestBase {

	@Test
	public void testsplit1(){
		assertEval("argv <- list(1:6, structure(1:2, .Label = c(\'1\', \'2\'), class = \'factor\')); .Internal(split(argv[[1]], argv[[2]]))");
	}

	@Test
	public void testsplit2(){
		assertEval("argv <- list(structure(c(-1.13864415195445, 0.574433648632919, 0.61125743366192, 0.291104607198021, 1.44367450704693, 0.408616385050392, -0.252815027721455, 0.73544465026571, -0.711029326417947, -0.611820918993561, -0.963259276248408, -0.28935033673839, -0.482346616963731, 0.575284398820533, 0.0664802498883062, 0.0889008730409177, -0.527009741835806, -0.572160487726669, 0.458433701366337, 0.0259549922279207, 0.79246010222197, 0.200856719794408, 0.681708382019133, 1.24959713166139, 2.28504683598586, 0.885201117877485, 0.275047494865858, 0.0611635446204713, -0.226340664609402, -0.701634984067551, -1.36331112409512, -0.470720710127998, 0.0116712292760789, 0.680960771805182, 1.25010637890252, -0.168484448953506, -0.703880448859559, -0.342493773069341, -0.359950801091045, 0.187018301421814, 0.332611568778467, 0.418088885897922, 1.52526747601546, 1.23881025318897, 1.97893910443604, 1.67980257496383, -0.0423836378777035, -0.085940264442189, 0.0751591419566941, 1.33615888669544, 1.29143414265875, 0.211686019646981, 0.107754613497605, -0.842122877395922, -0.363550334633855, -1.04260396788242, -1.00216202750311, -0.725219786582336, -0.702075395338802, -0.0588517433215815, 0.676181221812652, 0.606054389584641, -0.0722001122493283, -0.565579974058951, -1.50420998542363, -1.38835023347894, -1.6387526999868, -1.22317617387598, -2.6439685322602, -1.50311594814139, 0.58539278534518, 0.476423420506994, -0.229810354321508, -0.669629539423225, -0.500767918117353, -1.30780681405878, -0.0658147956438969, 0.619743292251259, 0.947409254626009, 0.137968713284014, -0.0705143536229389, -0.316245167388448, 0.423768217540825, -1.77132723836955, 0.437524374017483, 1.05217040293853, 1.29145821945076, 0.189519814277623, 0.405463210651828, -1.10579240546022, 0.470126971026959, 1.3013241742778, 1.57690948154138, 0.836753145709701, -0.0990436481848584, 0.305059193206195, 0.722542224965483, 0.497020187014643, -0.798519685959293, -0.162044448918511, -0.268976403560686, 0.471344909208507, 1.07960447064393, 0.816448434674936, 1.01857006703316, -0.19352270657549, 0.193745914189151, -0.0118346974247015, 0.515110447770272, 1.29117007883295, 0.484844437955959, 0.357506193819553, -1.95817055695569, -1.62102859205691, -2.13900473718215, -2.19173201733318, -1.86372596557808, -1.18864210270607, -1.19890597040604, 0.432503235072499, 0.594410727524479, 1.21432538936706, 2.15795981275539, 1.31528364302187, 0.38422055227912, 0.786869292659675, -0.703717985086569, -0.535651668024763, 0.34627858116184, 0.537117318247469, 0.901014803953916, 1.2151594352426, 0.827351473348557, -0.682186392255085, -1.33342351753519, -1.26893821314864, -1.4632463728941, -1.64736130434257, -1.21073183651285, -0.643396605364174), .Names = c(\'1\', \'2\', \'3\', \'4\', \'5\', \'6\', \'7\', \'8\', \'9\', \'10\', \'11\', \'12\', \'13\', \'14\', \'15\', \'16\', \'17\', \'18\', \'19\', \'20\', \'21\', \'22\', \'23\', \'24\', \'25\', \'26\', \'27\', \'28\', \'29\', \'30\', \'31\', \'32\', \'33\', \'34\', \'35\', \'36\', \'37\', \'38\', \'39\', \'40\', \'41\', \'42\', \'43\', \'44\', \'45\', \'46\', \'47\', \'48\', \'49\', \'50\', \'51\', \'52\', \'53\', \'54\', \'55\', \'56\', \'57\', \'58\', \'59\', \'60\', \'61\', \'62\', \'63\', \'64\', \'65\', \'66\', \'67\', \'68\', \'69\', \'70\', \'71\', \'72\', \'73\', \'74\', \'75\', \'76\', \'77\', \'78\', \'79\', \'80\', \'81\', \'82\', \'83\', \'84\', \'85\', \'86\', \'87\', \'88\', \'89\', \'90\', \'91\', \'92\', \'93\', \'94\', \'95\', \'96\', \'97\', \'98\', \'99\', \'100\', \'101\', \'102\', \'103\', \'104\', \'105\', \'106\', \'107\', \'108\', \'109\', \'110\', \'111\', \'112\', \'113\', \'114\', \'115\', \'116\', \'117\', \'118\', \'119\', \'120\', \'121\', \'122\', \'123\', \'124\', \'125\', \'126\', \'127\', \'128\', \'129\', \'130\', \'131\', \'132\', \'133\', \'134\', \'135\', \'136\', \'137\', \'138\', \'139\', \'140\')), structure(c(9L, 9L, 9L, 9L, 9L, 9L, 9L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 5L, 5L, 5L, 5L, 5L, 5L, 5L, 10L, 10L, 10L, 10L, 10L, 10L, 10L, 6L, 6L, 6L, 6L, 6L, 6L, 6L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 8L, 8L, 8L, 8L, 8L, 8L, 8L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 7L, 7L, 7L, 7L, 7L, 7L, 7L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 13L, 13L, 13L, 13L, 13L, 13L, 13L, 14L, 14L, 14L, 14L, 14L, 14L, 14L, 16L, 16L, 16L, 16L, 16L, 16L, 16L, 17L, 17L, 17L, 17L, 17L, 17L, 17L, 19L, 19L, 19L, 19L, 19L, 19L, 19L, 15L, 15L, 15L, 15L, 15L, 15L, 15L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 18L, 18L, 18L, 18L, 18L, 18L, 18L, 20L, 20L, 20L, 20L, 20L, 20L, 20L, 12L, 12L, 12L, 12L, 12L, 12L, 12L), .Label = c(\'10\', \'8\', \'2\', \'6\', \'3\', \'5\', \'9\', \'7\', \'1\', \'4\', \'17\', \'20\', \'11\', \'12\', \'16\', \'13\', \'14\', \'18\', \'15\', \'19\'), class = c(\'ordered\', \'factor\'))); .Internal(split(argv[[1]], argv[[2]]))");
	}

	@Test
	public void testsplit3(){
		assertEval("argv <- list(structure(c(1034.46153846154, 480.076923076923, 504.692307692308, 480.076923076923, 0, 0, 0, 0, 480.076923076923, 517.230769230769, 444.307692307692, 819.846153846154, 945.230769230769, 542.769230769231, 0, 0, 0, 1824.30769230769, 444.307692307692, 912.153846153846, 0, 0, 1514.07692307692, 0, 936, 0), .Dim = 26L, .Dimnames = list(c(\'1\', \'1\', \'1\', \'1\', \'1\', \'1\', \'1\', \'1\', \'1\', \'1\', \'1\', \'1\', \'1\', \'2\', \'2\', \'2\', \'2\', \'2\', \'2\', \'2\', \'2\', \'2\', \'2\', \'2\', \'2\', \'2\'))), structure(c(1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L), .Label = c(\'1\', \'2\'), class = \'factor\')); .Internal(split(argv[[1]], argv[[2]]))");
	}

	@Test
	public void testsplit4(){
		assertEval("argv <- list(structure(c(0, 0, 0, 0, 0, 0, 1.48219693752374e-323, 0, 0, 0, 0, 0), .Dim = c(1L, 12L), .Dimnames = list(NULL, c(\'1\', \'1\', \'1\', \'1\', \'1\', \'1\', \'1\', \'1\', \'1\', \'1\', \'1\', \'1\'))), structure(c(1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L), .Label = \'1\', class = \'factor\')); .Internal(split(argv[[1]], argv[[2]]))");
	}

	@Test
	public void testsplit5(){
		assertEval("argv <- list(c(1, 3, 5, 7, 8, 3, 5, NA, 4, 5, 7, 9), structure(c(8L, 6L, 3L, 2L, NA, 5L, 1L, 4L, 7L, 3L, NA, NA), .Label = c(\'0\', \'2\', \'6\', \'8\', \'15\', \'22\', \'29\', \'35\'), class = \'factor\')); .Internal(split(argv[[1]], argv[[2]]))");
	}

	@Test
	public void testsplit6(){
		assertEval("argv <- list(c(41L, 36L, 12L, 18L, NA, 28L, 23L, 19L, 8L, NA, 7L, 16L, 11L, 14L, 18L, 14L, 34L, 6L, 30L, 11L, 1L, 11L, 4L, 32L, NA, NA, NA, 23L, 45L, 115L, 37L, NA, NA, NA, NA, NA, NA, 29L, NA, 71L, 39L, NA, NA, 23L, NA, NA, 21L, 37L, 20L, 12L, 13L, NA, NA, NA, NA, NA, NA, NA, NA, NA, NA, 135L, 49L, 32L, NA, 64L, 40L, 77L, 97L, 97L, 85L, NA, 10L, 27L, NA, 7L, 48L, 35L, 61L, 79L, 63L, 16L, NA, NA, 80L, 108L, 20L, 52L, 82L, 50L, 64L, 59L, 39L, 9L, 16L, 78L, 35L, 66L, 122L, 89L, 110L, NA, NA, 44L, 28L, 65L, NA, 22L, 59L, 23L, 31L, 44L, 21L, 9L, NA, 45L, 168L, 73L, NA, 76L, 118L, 84L, 85L, 96L, 78L, 73L, 91L, 47L, 32L, 20L, 23L, 21L, 24L, 44L, 21L, 28L, 9L, 13L, 46L, 18L, 13L, 24L, 16L, 13L, 23L, 36L, 7L, 14L, 30L, NA, 14L, 18L, 20L), structure(c(1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 5L, 5L, 5L, 5L, 5L, 5L, 5L, 5L, 5L, 5L, 5L, 5L, 5L, 5L, 5L, 5L, 5L, 5L, 5L, 5L, 5L, 5L, 5L, 5L, 5L, 5L, 5L, 5L, 5L, 5L), .Label = c(\'1\', \'2\', \'3\', \'4\', \'5\'), class = \'factor\')); .Internal(split(argv[[1]], argv[[2]]))");
	}

	@Test
	public void testsplit7(){
		assertEval("argv <- list(structure(c(\'Sex/(Age + Eth * Lrn)\', \'Sex + Sex:Age + Sex:Eth + Sex:Lrn + Sex:Eth:Lrn + Sex:Age:Lrn\', \'Sex + Sex:Age + Sex:Eth + Sex:Lrn + Sex:Eth:Lrn + Sex:Age:Lrn + Sex:Age:Eth + Sex:Age:Eth:Lrn\', \'1.597991\', \'1.686899\', \'1.928360\', \'132\', \'128\', \'118\', \'-1063.025\', \'-1055.398\', \'-1039.324\', \'\', \'1 vs 2\', \'2 vs 3\', \'\', \' 4\', \'10\', \'\', \' 7.627279\', \'16.073723\', \'\', \'0.10622602\', \'0.09754136\'), .Dim = c(3L, 8L)), structure(c(1L, 1L, 1L, 2L, 2L, 2L, 3L, 3L, 3L, 4L, 4L, 4L, 5L, 5L, 5L, 6L, 6L, 6L, 7L, 7L, 7L, 8L, 8L, 8L), .Label = c(\'1\', \'2\', \'3\', \'4\', \'5\', \'6\', \'7\', \'8\'), class = \'factor\')); .Internal(split(argv[[1]], argv[[2]]))");
	}

	@Test
	public void testsplit8(){
		assertEval("argv <- list(c(0L, 0L, 0L, 1L, 0L, 0L, 0L, 1L, 0L, 0L, 0L, 1L, 0L, 0L, 0L, 1L, 0L, 0L, 0L, 1L, 0L, 0L, 0L, 1L, 0L, 0L, 0L, 1L, 0L, 0L, 0L, 1L, 0L, 0L, 0L, 1L, 0L, 0L, 0L, 1L, 0L, 0L, 0L, 1L, 0L, 0L, 0L, 1L, 0L, 0L, 0L, 1L, 0L, 0L, 0L, 1L, 0L, 0L, 0L, 1L, 0L, 0L, 0L, 1L, 0L, 0L, 0L, 1L, 0L, 0L, 0L, 1L, 0L, 0L, 0L, 1L, 0L, 0L, 0L, 1L, 0L, 0L, 0L, 1L, 0L, 0L, 0L, 1L, 0L, 0L, 0L, 1L, 0L, 0L, 0L, 1L, 0L, 0L, 0L, 1L, 0L, 0L, 0L, 1L, 0L, 0L, 0L, 1L, 0L, 0L, 0L, 1L, 0L, 0L, 0L, 1L, 0L, 0L, 0L, 1L, 0L, 0L, 0L, 1L, 0L, 0L, 0L, 1L, 0L, 0L, 0L, 1L, 0L, 0L, 0L, 1L, 0L, 0L, 0L, 1L, 0L, 0L, 0L, 1L, 0L, 0L, 0L, 1L, 0L, 0L, 0L, 1L, 0L, 0L, 0L, 1L, 0L, 0L, 0L, 1L, 0L, 0L, 0L, 1L, 0L, 0L, 0L, 1L, 0L, 0L, 0L, 1L, 0L, 0L, 0L, 1L, 0L, 0L, 0L, 1L, 0L, 0L, 0L, 1L, 0L, 0L, 0L, 1L, 0L, 0L, 0L, 1L, 0L, 0L, 0L, 1L, 0L, 0L, 0L, 1L, 0L, 0L, 0L, 1L, 0L, 0L, 0L, 1L, 0L, 0L, 0L, 1L, 0L, 0L, 0L, 1L, 0L, 0L, 0L, 1L, 0L, 0L, 0L, 1L, 0L, 0L, 0L, 1L, 0L, 0L, 0L, 1L, 0L, 0L, 0L, 1L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L), structure(c(60L, 60L, 60L, 60L, 61L, 61L, 61L, 61L, 62L, 62L, 62L, 62L, 63L, 63L, 63L, 63L, 64L, 64L, 64L, 64L, 65L, 65L, 65L, 65L, 66L, 66L, 66L, 66L, 67L, 67L, 67L, 67L, 68L, 68L, 68L, 68L, 69L, 69L, 69L, 69L, 70L, 70L, 70L, 70L, 71L, 71L, 71L, 71L, 72L, 72L, 72L, 72L, 73L, 73L, 73L, 73L, 74L, 74L, 74L, 74L, 75L, 75L, 75L, 75L, 76L, 76L, 76L, 76L, 77L, 77L, 77L, 77L, 78L, 78L, 78L, 78L, 79L, 79L, 79L, 79L, 80L, 80L, 80L, 80L, 81L, 81L, 81L, 81L, 82L, 82L, 82L, 82L, 83L, 83L, 83L, 83L, 84L, 84L, 84L, 84L, 85L, 85L, 85L, 85L, 86L, 86L, 86L, 86L, 87L, 87L, 87L, 87L, 88L, 88L, 88L, 88L, 89L, 89L, 89L, 89L, 90L, 90L, 90L, 90L, 91L, 91L, 91L, 91L, 92L, 92L, 92L, 92L, 93L, 93L, 93L, 93L, 94L, 94L, 94L, 94L, 95L, 95L, 95L, 95L, 96L, 96L, 96L, 96L, 97L, 97L, 97L, 97L, 98L, 98L, 98L, 98L, 99L, 99L, 99L, 99L, 100L, 100L, 100L, 100L, 101L, 101L, 101L, 101L, 102L, 102L, 102L, 102L, 103L, 103L, 103L, 103L, 104L, 104L, 104L, 104L, 105L, 105L, 105L, 105L, 106L, 106L, 106L, 106L, 107L, 107L, 107L, 107L, 108L, 108L, 108L, 108L, 109L, 109L, 109L, 109L, 110L, 110L, 110L, 110L, 111L, 111L, 111L, 111L, 112L, 112L, 112L, 112L, 113L, 113L, 113L, 113L, 114L, 114L, 114L, 114L, 115L, 115L, 115L, 115L, 116L, 116L, 116L, 116L, 117L, 117L, 117L, 117L, 118L, 118L, 118L, 118L, 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L, 11L, 12L, 13L, 14L, 15L, 16L, 17L, 18L, 19L, 20L, 21L, 22L, 23L, 24L, 25L, 26L, 27L, 28L, 29L, 30L, 31L, 32L, 33L, 34L, 35L, 36L, 37L, 38L, 39L, 40L, 41L, 42L, 43L, 44L, 45L, 46L, 47L, 48L, 49L, 50L, 51L, 52L, 53L, 54L, 55L, 56L, 57L, 58L, 59L), .Label = c(\'0\', \'1\', \'2\', \'3\', \'4\', \'5\', \'6\', \'7\', \'8\', \'9\', \'10\', \'11\', \'12\', \'13\', \'14\', \'15\', \'16\', \'17\', \'18\', \'19\', \'20\', \'21\', \'22\', \'23\', \'24\', \'25\', \'26\', \'27\', \'28\', \'29\', \'30\', \'31\', \'32\', \'33\', \'34\', \'35\', \'36\', \'37\', \'38\', \'39\', \'40\', \'41\', \'42\', \'43\', \'44\', \'45\', \'46\', \'47\', \'48\', \'49\', \'50\', \'51\', \'52\', \'53\', \'54\', \'55\', \'56\', \'57\', \'58\', \'59\', \'60\', \'61\', \'62\', \'63\', \'64\', \'65\', \'66\', \'67\', \'68\', \'69\', \'70\', \'71\', \'72\', \'73\', \'74\', \'75\', \'76\', \'77\', \'78\', \'79\', \'80\', \'81\', \'82\', \'83\', \'84\', \'85\', \'86\', \'87\', \'88\', \'89\', \'90\', \'91\', \'92\', \'93\', \'94\', \'95\', \'96\', \'97\', \'98\', \'99\', \'100\', \'101\', \'102\', \'103\', \'104\', \'105\', \'106\', \'107\', \'108\', \'109\', \'110\', \'111\', \'112\', \'113\', \'114\', \'115\', \'116\', \'117\'), class = \'factor\')); .Internal(split(argv[[1]], argv[[2]]))");
	}

	@Test
	public void testsplit9(){
		assertEval("argv <- list(structure(c(NA, NA), .Dim = 1:2), structure(1:2, .Label = c(\'1\', \'2\'), class = \'factor\')); .Internal(split(argv[[1]], argv[[2]]))");
	}

	@Test
	public void testsplit10(){
		assertEval("argv <- list(c(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0), structure(c(2L, 3L, 4L, 1L, 2L, 3L, 4L, 1L, 2L, 3L, 4L, 1L, 2L, 3L, 4L, 1L, 2L, 3L, 4L, 1L, 2L, 3L, 4L, 1L, 2L, 3L, 4L, 1L, 2L, 3L, 4L, 1L, 2L, 3L, 4L, 1L, 2L, 3L, 4L, 1L, 2L, 3L, 4L, 1L, 2L, 3L, 4L, 1L, 2L, 3L, 4L, 1L, 2L, 3L, 4L, 1L, 2L, 3L, 4L, 1L, 2L, 3L, 4L, 1L, 2L, 3L, 4L, 1L, 2L, 3L, 4L), .Label = c(\'1\', \'2\', \'3\', \'4\'), class = \'factor\')); .Internal(split(argv[[1]], argv[[2]]))");
	}

	@Test
	public void testsplit12(){
		assertEval("argv <- list(structure(c(95.7845839266016, 111.117917259935, 120.284583926602, 77.019531700964, 96.9521364368474, 112.285469770181, 121.452136436847, 77.019531700964, 96.9521364368474, 112.285469770181, 121.452136436847, 78.1870842112099, 98.1196889470933, 113.453022280427, 122.619688947093, 79.3546367214558, 99.2872414573392, 114.620574790673, 123.787241457339, 79.3546367214558, 99.2872414573392, 114.620574790673, 123.787241457339, 78.1870842112099, 98.1196889470933, 113.453022280427, 122.619688947093, 79.3546367214558, 99.2872414573392, 114.620574790673, 123.787241457339, 79.3546367214558, 99.2872414573392, 114.620574790673, 123.787241457339, 78.1870842112099, 98.1196889470933, 113.453022280427, 122.619688947093, 79.3546367214558, 99.2872414573392, 114.620574790673, 123.787241457339, 79.3546367214558, 99.2872414573392, 114.620574790673, 123.787241457339, 78.1870842112099, 98.1196889470933, 113.453022280427, 122.619688947093, 79.3546367214558, 99.2872414573392, 114.620574790673, 123.787241457339, 79.3546367214558, 99.2872414573392, 114.620574790673, 123.787241457339, 78.1870842112099, 98.1196889470933, 113.453022280427, 122.619688947093, 79.3546367214558, 99.2872414573392, 114.620574790673, 123.787241457339, 79.3546367214558, 99.2872414573392, 114.620574790673, 123.787241457339), .Dim = c(71L, 1L), .Dimnames = list(c(\'2\', \'3\', \'4\', \'5\', \'6\', \'7\', \'8\', \'9\', \'10\', \'11\', \'12\', \'13\', \'14\', \'15\', \'16\', \'17\', \'18\', \'19\', \'20\', \'21\', \'22\', \'23\', \'24\', \'25\', \'26\', \'27\', \'28\', \'29\', \'30\', \'31\', \'32\', \'33\', \'34\', \'35\', \'36\', \'37\', \'38\', \'39\', \'40\', \'41\', \'42\', \'43\', \'44\', \'45\', \'46\', \'47\', \'48\', \'49\', \'50\', \'51\', \'52\', \'53\', \'54\', \'55\', \'56\', \'57\', \'58\', \'59\', \'60\', \'61\', \'62\', \'63\', \'64\', \'65\', \'66\', \'67\', \'68\', \'69\', \'70\', \'71\', \'72\'), NULL)), structure(c(2L, 3L, 4L, 1L, 2L, 3L, 4L, 1L, 2L, 3L, 4L, 1L, 2L, 3L, 4L, 1L, 2L, 3L, 4L, 1L, 2L, 3L, 4L, 1L, 2L, 3L, 4L, 1L, 2L, 3L, 4L, 1L, 2L, 3L, 4L, 1L, 2L, 3L, 4L, 1L, 2L, 3L, 4L, 1L, 2L, 3L, 4L, 1L, 2L, 3L, 4L, 1L, 2L, 3L, 4L, 1L, 2L, 3L, 4L, 1L, 2L, 3L, 4L, 1L, 2L, 3L, 4L, 1L, 2L, 3L, 4L), .Label = c(\'1\', \'2\', \'3\', \'4\'), class = \'factor\')); .Internal(split(argv[[1]], argv[[2]]))");
	}

	@Test
	public void testsplit14(){
		assertEval("argv <- list(structure(c(123.48457192908, 239.059434652297, 290.055338401838, 18.397281603467, 6.57585722655537, 0.670931786731845, 0.178466148156965, 0.245410750178149, 0.363167328274208, 0.194808268742596, 2172.67583033103, 8.91763605923317e+38), .Dim = c(1L, 12L), .Dimnames = list(NULL, c(\'1\', \'1\', \'1\', \'1\', \'1\', \'1\', \'1\', \'1\', \'1\', \'1\', \'1\', \'1\'))), structure(c(1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L), .Label = \'1\', class = \'factor\')); .Internal(split(argv[[1]], argv[[2]]))");
	}

	@Test
	public void testsplit15(){
		assertEval("argv <- list(character(0), structure(integer(0), .Label = character(0), class = \'factor\')); .Internal(split(argv[[1]], argv[[2]]))");
	}

	@Test
	public void testsplit16(){
		assertEval("argv <- list(structure(c(47.432, 12.482), .Names = c(\'(Intercept)\', \'group2\')), structure(1:2, .Label = c(\'0\', \'1\'), class = \'factor\')); .Internal(split(argv[[1]], argv[[2]]))");
	}

	@Test
	public void testsplit17(){
		assertEval("argv <- list(c(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 26.3011774151224, 2.485452029809, 7.15323925419351, 16.761819986295, 10.2645644917686, 0.758337657329402, 29.4935619829433, 12.665970880074, 2.27782676164194e-08, 0.115876279686418), structure(c(1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 4L, 5L, 5L, 5L, 5L, 5L, 5L, 5L, 5L, 5L, 5L, 5L, 5L, 5L, 5L, 5L, 5L, 5L, 5L, 5L, 5L, 5L, 5L, 5L, 5L, 5L, 6L, 6L, 6L, 6L, 6L, 6L, 6L, 6L, 6L, 6L, 6L, 6L, 6L, 6L, 6L, 6L, 6L, 6L, 6L, 6L, 6L, 6L, 6L, 6L, 6L, 6L, 6L, 6L, 6L, 6L, 7L, 7L, 7L, 7L, 7L, 7L, 7L, 7L, 7L, 7L, 7L, 7L, 7L, 7L, 7L, 7L, 7L, 7L, 7L, 7L, 7L, 7L, 7L, 7L, 7L, 7L, 7L, 7L, 7L, 7L, 7L, 7L, 7L, 8L, 8L, 8L, 8L, 8L, 8L, 8L, 9L, 9L, 9L, 9L, 9L, 9L, 9L, 9L, 9L, 9L, 9L, 9L, 10L, 10L, 10L, 10L, 10L, 10L, 10L, 10L, 10L, 10L, 10L, 10L, 10L, 10L, 10L, 10L, 10L, 10L, 10L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 11L, 12L, 13L, 14L, 15L, 16L, 17L, 18L, 19L, 20L, 21L), .Label = c(\'1\', \'3\', \'5\', \'7\', \'9\', \'11\', \'13\', \'15\', \'17\', \'19\', \'21\', \'23\', \'25\', \'27\', \'29\', \'31\', \'33\', \'35\', \'37\', \'39\', \'41\'), class = \'factor\')); .Internal(split(argv[[1]], argv[[2]]))");
	}
}
