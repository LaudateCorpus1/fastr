/*
 * This material is distributed under the GNU General Public License
 * Version 2. You may review the terms of this license at
 * http://www.gnu.org/licenses/gpl-2.0.html
 * 
 * Copyright (c) 2014, Purdue University
 * Copyright (c) 2014, Oracle and/or its affiliates
 *
 * All rights reserved.
 */
package com.oracle.truffle.r.test.testrgen;

import org.junit.*;

import com.oracle.truffle.r.test.*;

public class TestrGenBuiltinisnumeric extends TestBase {

    @Test
    public void testisnumeric1() {
        assertEval("argv <- list(structure(c(4.17, 5.58, 5.18, 6.11, 4.5, 4.61, 5.17, 4.53, 5.33, 5.14, 4.81, 4.17, 4.41, 3.59, 5.87, 3.83, 6.03, 4.89, 4.32, 4.69, 17.3889, 31.1364, 26.8324, 37.3321, 20.25, 21.2521, 26.7289, 20.5209, 28.4089, 26.4196, 23.1361, 17.3889, 19.4481, 12.8881, 34.4569, 14.6689, 36.3609, 23.9121, 18.6624, 21.9961), .Dim = c(20L, 2L), .Dimnames = list(NULL, c(\'w\', \'w2\'))));is.numeric(argv[[1]]);");
    }

    @Test
    @Ignore
    public void testisnumeric2() {
        assertEval("argv <- list(structure(c(12784, 12874, 12965, 13057, 13149, 13239, 13330, 13422, 13514, 13604, 13695, 13787, 13879, 13970, 14061, 14153, 14245, 14335), class = \'Date\'));is.numeric(argv[[1]]);");
    }

    @Test
    public void testisnumeric3() {
        assertEval("argv <- list(c(1.4615016373309e+48, 5.70899077082384e+45, 2.23007451985306e+43, 8.71122859317602e+40, 3.40282366920938e+38, 1.32922799578492e+36, 5.19229685853483e+33, 2.02824096036517e+31, 7.92281625142643e+28, 3.09485009821345e+26, 1.20892581961463e+24, 4.72236648286965e+21, 18446744073709551616, 72057594037927936, 281474976710656, 1099511627776, 4294967296, 16777216, 65536, 256, 1, 0.00390625, 1.52587890625e-05, 5.96046447753906e-08, 2.3283064365387e-10, 9.09494701772928e-13, 3.5527136788005e-15, 1.38777878078145e-17, 5.42101086242752e-20, 2.11758236813575e-22, 8.27180612553028e-25));is.numeric(argv[[1]]);");
    }

    @Test
    public void testisnumeric4() {
        assertEval("argv <- list(integer(0));is.numeric(argv[[1]]);");
    }

    @Test
    public void testisnumeric5() {
        assertEval("argv <- list(c(16.4, 11.4, 7.8, 14, 10.9, 16.8, 16.6, 5.9, 21));is.numeric(argv[[1]]);");
    }

    @Test
    public void testisnumeric6() {
        assertEval("argv <- list(c(0, 0, 0, 1, 0, 0, 0, 0, 1, 0, NA, NA, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 0, 1, 1, 0, 1, NA, 1, 0, 1, 0, NA, 1, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, NA, 0, 1, 0, 0, 0, 0, NA, 1, 1));is.numeric(argv[[1]]);");
    }

    @Test
    public void testisnumeric7() {
        assertEval("argv <- list(c(0, 0, 0, 0, 0, 1.75368801162502e-134, 0, 0, 0, 2.60477585273833e-251, 1.16485035372295e-260, 0, 1.53160350210786e-322, 0.333331382328728, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3.44161262707711e-123, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1.968811545398e-173, 0, 8.2359965384697e-150, 0, 0, 0, 0, 6.51733217171341e-10, 0, 2.36840184577368e-67, 0, 9.4348408357524e-307, 0, 1.59959906013771e-89, 0, 8.73836857865034e-286, 7.09716190970992e-54, 0, 0, 0, 1.530425353017e-274, 8.57590058044551e-14, 0.333333106397154, 0, 0, 1.36895217898448e-199, 2.0226102635783e-177, 5.50445388209462e-42, 0, 0, 0, 0, 1.07846402051283e-44, 1.88605464411243e-186, 1.09156111051203e-26, 0, 3.0702877273237e-124, 0.333333209689785, 0, 0, 0, 0, 0, 0, 3.09816093866831e-94, 0, 0, 4.7522727332095e-272, 0, 0, 2.30093251441394e-06, 0, 0, 1.27082826644707e-274, 0, 0, 0, 0, 0, 0, 0, 4.5662025456054e-65, 0, 2.77995853978268e-149, 0, 0, 0));is.numeric(argv[[1]]);");
    }

    @Test
    @Ignore
    public void testisnumeric8() {
        assertEval("argv <- list(structure(c(1L, 2L, 3L, 4L, 1L, 2L, 3L, 4L, 1L, 2L, 3L, 4L, 1L, 2L, 3L, 4L, 1L, 2L, 3L, 4L, 1L, 2L, 3L, 4L, 1L, 2L, 3L, 4L, 1L, 2L, 3L, 4L, 1L, 2L, 3L, 4L, 1L, 2L, 3L, 4L, 1L, 2L, 3L, 4L, 1L, 2L, 3L, 4L, 1L, 2L, 3L, 4L, 1L, 2L, 3L, 4L, 1L, 2L, 3L, 4L, 1L, 2L, 3L, 4L, 1L, 2L, 3L, 4L, 1L, 2L, 3L, 4L, 1L, 2L, 3L, 4L, 1L, 2L, 3L, 4L), .Label = c(\'1\', \'2\', \'3\', \'4\'), class = \'factor\'));is.numeric(argv[[1]]);");
    }

    @Test
    public void testisnumeric9() {
        assertEval("argv <- list(structure(list(a_string = c(\'foo\', \'bar\'), a_bool = FALSE, a_struct = structure(list(a = 1, b = structure(c(1, 3, 2, 4), .Dim = c(2L, 2L)), c = \'foo\'), .Names = c(\'a\', \'b\', \'c\')), a_cell = structure(list(1, \'foo\', structure(c(1, 3, 2, 4), .Dim = c(2L, 2L)), \'bar\'), .Dim = c(2L, 2L)), a_complex_scalar = 0+1i, a_list = list(1, structure(c(1, 3, 2, 4), .Dim = c(2L, 2L)), \'foo\'), a_complex_matrix = structure(c(1+2i, 5+0i, 3-4i, -6+0i), .Dim = c(2L, 2L)), a_range = c(1, 2, 3, 4, 5), a_scalar = 1,     a_complex_3_d_array = structure(c(1+1i, 3+1i, 2+1i, 4+1i, 5-1i, 7-1i, 6-1i, 8-1i), .Dim = c(2L, 2L, 2L)), a_3_d_array = structure(c(1, 3, 2, 4, 5, 7, 6, 8), .Dim = c(2L, 2L, 2L)), a_matrix = structure(c(1, 3, 2, 4), .Dim = c(2L, 2L)), a_bool_matrix = structure(c(TRUE, FALSE, FALSE, TRUE), .Dim = c(2L, 2L))), .Names = c(\'a_string\', \'a_bool\', \'a_struct\', \'a_cell\', \'a_complex_scalar\', \'a_list\', \'a_complex_matrix\', \'a_range\', \'a_scalar\', \'a_complex_3_d_array\', \'a_3_d_array\', \'a_matrix\', \'a_bool_matrix\')));is.numeric(argv[[1]]);");
    }

    @Test
    public void testisnumeric10() {
        assertEval("argv <- list(structure(c(1L, 2L, NA, 3L), .Label = c(\'aa\', \'bb\', \'dd\')));is.numeric(argv[[1]]);");
    }

    @Test
    public void testisnumeric11() {
        assertEval("argv <- list(structure(c(1120, 1160, 963, 1210, 1160, 1160, 813, 1230, 1370, 1140, 995, 935, 1110, 994, 1020, 960, 1180, 799, 958, 1140, 1100, 1210, 1150, 1250, 1260, 1220, 1030, 1100, 774, 840, 874, 694, 940, 833, 701, 916, 692, 1020, 1050, 969, 831, 726, 456, 824, 702, 1120, 1100, 832, 764, 821, 768, 845, 864, 862, 698, 845, 744, 796, 1040, 759, 781, 865, 845, 944, 984, 897, 822, 1010, 771, 676, 649, 846, 812, 742, 801, 1040, 860, 874, 848, 890, 744, 749, 838, 1050, 918, 986, 797, 923, 975, 815, 1020, 906, 901, 1170, 912, 746, 919, 718, 714, 740), .Tsp = c(1871, 1970, 1), class = \'ts\'));is.numeric(argv[[1]]);");
    }

    @Test
    public void testisnumeric12() {
        assertEval("argv <- list(c(-0.667819876370237, 0.170711734013213, 0.552921941721332, -0.253162069270378, -0.00786394222146348, 0.0246733498130512, 0.0730305465518564, -1.36919169254062, 0.0881443844426084, -0.0834190388782434));is.numeric(argv[[1]]);");
    }

    @Test
    @Ignore
    public void testisnumeric13() {
        assertEval("argv <- list(structure(c(39.7, 27.7, 43.5, 89.7, 70.2, 63.5, 36.5, 15.2, 35.3, 45.2, 1.2, 49.5, 63.1, 34, 17, 17.6, 67.8, 45.1, 50.9, 64.9, 59.8, 73, 37.6, 60.7, 60.8, 69.3, 71.2, 64.5, 75.9, 7.7, 72.6, 18.7, 78.2, 55.1, 58.1, 62, 53.3, 85.9, 67.5, 16.7, 46.6, 84.9, 19.4, 38.4, 5, 22, 17, 5, 16, 6, 12, 31, 9, 16, 37, 15, 13, 17, 15, 35, 14, 6, 22, 7, 22, 19, 15, 19, 16, 22, 12, 14, 9, 29, 18, 25, 12, 14, 14, 21, 12, 3, 14, 22, 16, 7, 26, 26, 5, 29, 15, 2, 7, 3, 7, 20, 7, 13, 53, 8, 13, 8, 12, 32, 8, 9, 12, 3, 10, 9, 7, 12, 10, 5, 1, 6, 9, 11, 2, 7, 6, 3, 8, 12, 7, 2, 7, 13, 29, 6, 28, 12, 93.4, 58.33, 5.16, 100, 92.85, 2.56, 33.77, 2.15, 90.57, 91.38, 42.34, 6.1, 96.83, 3.3, 9.96, 16.92, 97.16, 84.84, 15.14, 98.22, 5.23, 2.84, 4.97, 4.43, 7.72, 2.82, 2.4, 98.61, 99.06, 13.79, 24.2, 8.65, 98.96, 4.52, 5.23, 8.52, 97.67, 99.71, 2.27, 11.22, 50.43, 99.68, 12.11, 5.62, 20.2, 19.3, 20.6, 18.3, 23.6, 18, 20.3, 10.8, 26.6, 24.4, 18, 22.5, 18.1, 20, 22.2, 23, 24.9, 22.2, 16.7, 20.2, 18, 20, 20, 22.7, 16.3, 18.7, 21, 24.5, 17.8, 20.5, 21.2, 19.5, 19.4, 22.4, 23.8, 16.5, 21, 15.1, 19.1, 18.9, 18.2, 19.8, 20.2, 20.3), .Dim = c(44L, 5L), .Dimnames = list(c(\'Franches-Mnt\', \'Rive Gauche\', \'Neuveville\', \'Herens\', \'Broye\', \'Paysd'enhaut\', \'Moutier\', \'La Vallee\', \'Porrentruy\', \'Sarine\', \'V. De Geneve\', \'Yverdon\', \'Sion\', \'Grandson\', \'Courtelary\', \'Neuchatel\', \'Glane\', \'Delemont\', \'Nyone\', \'Monthey\', \'Morges\', \'Lavaux\', \'Val de Ruz\', \'Avenches\', \'Rolle\', \'Cossonay\', \'Oron\', \'Veveyse\', \'St Maurice\', \'La Chauxdfnd\', \'Echallens\', \'ValdeTravers\', \'Martigwy\', \'Moudon\', \'Payerne\', \'Aigle\', \'Gruyere\', \'Conthey\', \'Aubonne\', \'Le Locle\', \'Rive Droite\', \'Entremont\', \'Lausanne\', \'Boudry\'), c(\'Agriculture\', \'Examination\', \'Education\', \'Catholic\', \'Infant.Mortality\'))));is.numeric(argv[[1]]);");
    }

    @Test
    @Ignore
    public void testisnumeric14() {
        assertEval("argv <- list(structure(c(1386439154.20645, 1386469154.20645, 1386499154.20645, 1386529154.20645, 1386559154.20645, 1386589154.20645, 1386619154.20645, 1386649154.20645, 1386679154.20645, 1386709154.20645, 1386739154.20645, 1386769154.20645, 1386799154.20645, 1386829154.20645, 1386859154.20645, 1386889154.20645, 1386919154.20645, 1386949154.20645, 1386979154.20645, 1387009154.20645, 1387039154.20645, 1387069154.20645, 1387099154.20645, 1387129154.20645, 1387159154.20645, 1387189154.20645, 1387219154.20645, 1387249154.20645, 1387279154.20645, 1387309154.20645, 1387339154.20645, 1387369154.20645, 1387399154.20645, 1387429154.20645), class = c(\'POSIXct\', \'POSIXt\')));is.numeric(argv[[1]]);");
    }

    @Test
    public void testisnumeric15() {
        assertEval("argv <- list(c(\'2001-01-01\', NA, NA, \'2004-10-26\'));is.numeric(argv[[1]]);");
    }

    @Test
    public void testisnumeric16() {
        assertEval("argv <- list(c(1, 0.987688340595138, 0.951056516295154, 0.891006524188368, 0.809016994374947, 0.707106781186547, 0.587785252292473, 0.453990499739547, 0.309016994374947, 0.156434465040231, -1.83697019872103e-16, -0.156434465040231, -0.309016994374948, -0.453990499739547, -0.587785252292473, -0.707106781186548, -0.809016994374948, -0.891006524188368, -0.951056516295154, -0.987688340595138, -1, -0.987688340595138, -0.951056516295154, -0.891006524188368, -0.809016994374947, -0.707106781186547, -0.587785252292473, -0.453990499739547, -0.309016994374947, -0.156434465040231, 6.12323399573677e-17, 0.156434465040231, 0.309016994374947, 0.453990499739547, 0.587785252292473, 0.707106781186548, 0.809016994374947, 0.891006524188368, 0.951056516295154, 0.987688340595138, 1, 0.987688340595138, 0.951056516295154, 0.891006524188368, 0.809016994374947, 0.707106781186548, 0.587785252292473, 0.453990499739547, 0.309016994374947, 0.156434465040231, 6.12323399573677e-17, -0.15643446504023, -0.309016994374947, -0.453990499739548, -0.587785252292473, -0.707106781186547, -0.809016994374947, -0.891006524188368, -0.951056516295154, -0.987688340595138, -1, -0.987688340595138, -0.951056516295154, -0.891006524188368, -0.809016994374948, -0.707106781186547, -0.587785252292473, -0.453990499739548, -0.309016994374948, -0.15643446504023, -1.83697019872103e-16, 0.15643446504023, 0.309016994374947, 0.453990499739547, 0.587785252292473, 0.707106781186547, 0.809016994374947, 0.891006524188368, 0.951056516295154, 0.987688340595138, 1, 0.987688340595138, 0.951056516295154, 0.891006524188368, 0.809016994374948, 0.707106781186547, 0.587785252292473, 0.453990499739548, 0.309016994374948, 0.15643446504023, 3.06161699786838e-16, -0.15643446504023, -0.309016994374947, -0.453990499739547, -0.587785252292473, -0.707106781186547, -0.809016994374947, -0.891006524188368, -0.951056516295153, -0.987688340595138, -1));is.numeric(argv[[1]]);");
    }

    @Test
    public void testisnumeric17() {
        assertEval("argv <- list(structure(c(0.696706709347165, 0.362357754476673, -0.0291995223012888, 0.696706709347165, 0.696706709347165, -0.0291995223012888, 0.696706709347165, -0.0291995223012888, 0.362357754476673, 0.696706709347165, -0.0291995223012888, 0.362357754476673, -0.416146836547142, 0.362357754476673, 0.696706709347165, 0.696706709347165, 0.362357754476673, -0.416146836547142, -0.0291995223012888, -0.416146836547142, 0.696706709347165, -0.416146836547142, 0.362357754476673, -0.0291995223012888, 0.717356090899523, 0.932039085967226, 0.999573603041505, 0.717356090899523, 0.717356090899523, 0.999573603041505, 0.717356090899523, 0.999573603041505, 0.932039085967226, 0.717356090899523, 0.999573603041505, 0.932039085967226, 0.909297426825682, 0.932039085967226, 0.717356090899523, 0.717356090899523, 0.932039085967226, 0.909297426825682, 0.999573603041505, 0.909297426825682, 0.717356090899523, 0.909297426825682, 0.932039085967226, 0.999573603041505, -0.0291995223012888, -0.737393715541246, -0.998294775794753, -0.0291995223012888, -0.0291995223012888, -0.998294775794753, -0.0291995223012888, -0.998294775794753, -0.737393715541246, -0.0291995223012888, -0.998294775794753, -0.737393715541246, -0.653643620863612, -0.737393715541246, -0.0291995223012888, -0.0291995223012888, -0.737393715541246, -0.653643620863612, -0.998294775794753, -0.653643620863612, -0.0291995223012888, -0.653643620863612, -0.737393715541246, -0.998294775794753, 0.999573603041505, 0.67546318055115, -0.0583741434275801, 0.999573603041505, 0.999573603041505, -0.0583741434275801, 0.999573603041505, -0.0583741434275801, 0.67546318055115, 0.999573603041505, -0.0583741434275801, 0.67546318055115, -0.756802495307928, 0.67546318055115, 0.999573603041505, 0.999573603041505, 0.67546318055115, -0.756802495307928, -0.0583741434275801, -0.756802495307928, 0.999573603041505, -0.756802495307928, 0.67546318055115, -0.0583741434275801), .Dim = c(24L, 4L), .Dimnames = list(NULL, c(\'A\', \'B\', \'C\', \'D\'))));is.numeric(argv[[1]]);");
    }

    @Test
    public void testisnumeric18() {
        assertEval("argv <- list(c(1, 1, NA, 2));is.numeric(argv[[1]]);");
    }

    @Test
    public void testisnumeric19() {
        assertEval("argv <- list(structure(c(79.5323303457107, 6, 86.1989970123773, 6, 69.7732394366197, 5, 98.0323303457106, 6, 108.032330345711, 6, 89.1989970123773, 6, 114.198997012377, 6, 116.698997012377, 6, 110.365663679044, 6, 124.365663679044, 6, 126.365663679044, 6, 118.032330345711, 6), .Dim = c(6L, 4L), .Dimnames = structure(list(V = c(\'Golden.rain\', \'rep        \', \'Marvellous \', \'rep        \', \'Victory    \', \'rep        \'), N = c(\'0.0cwt\', \'0.2cwt\', \'0.4cwt\', \'0.6cwt\')), .Names = c(\'V\', \'N\'))));is.numeric(argv[[1]]);");
    }

    @Test
    public void testisnumeric20() {
        assertEval("argv <- list(structure(numeric(0), .Dim = c(3L, 0L)));is.numeric(argv[[1]]);");
    }

    @Test
    @Ignore
    public void testisnumeric21() {
        assertEval("argv <- list(structure(16146, class = \'Date\'));is.numeric(argv[[1]]);");
    }

    @Test
    public void testisnumeric22() {
        assertEval("argv <- list(structure(c(-3.001e+155, -1.067e+107, -1.976e+62, -9.961e+152, -2.059e+23, 0.5104), .Names = c(\'Min.\', \'1st Qu.\', \'Median\', \'Mean\', \'3rd Qu.\', \'Max.\'), class = c(\'summaryDefault\', \'table\')));is.numeric(argv[[1]]);");
    }
}
