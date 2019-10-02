package com.evilduck.enums;

public enum AlphabetEmojis {

    A("\uD83C\uDDE6"),
    B("\uD83C\uDDE7"),
    C("\uD83C\uDDE8"),
    D("\uD83C\uDDE9"),
    E("\uD83C\uDDEA"),
    F("\uD83C\uDDEB"),
    G("\uD83C\uDDEC"),
    H("\uD83C\uDDED"),
    I("\uD83C\uDDEE"),
    J("\uD83C\uDDEF"),
    K("\uD83C\uDDF0"),
    L("\uD83C\uDDF1"),
    M("\uD83C\uDDF2"),
    N("\uD83C\uDDF3"),
    O("\uD83C\uDDF4"),
    P("\uD83C\uDDF5"),
    Q("\uD83C\uDDF6"),
    R("\uD83C\uDDF7"),
    S("\uD83C\uDDF8"),
    T("\uD83C\uDDF9"),
    U("\uD83C\uDDFA"),
    V("\uD83C\uDDFB"),
    W("\uD83C\uDDFC"),
    X("\uD83C\uDDFD"),
    Y("\uD83C\uDDFE"),
    Z("\uD83C\uDDFF");


    private final String unicode;

    AlphabetEmojis(final String unicode) {this.unicode = unicode;}

    public static String unicodeForLetter(final Character letter) {
        switch (letter) {
            case 'a' | 1: return A.getUnicode();
            case 'b' | 2: return B.getUnicode();
            case 'c' | 3: return C.getUnicode();
            case 'd' | 4: return D.getUnicode();
            case 'e' | 5: return E.getUnicode();
            case 'f' | 6: return F.getUnicode();
            case 'g' | 7: return G.getUnicode();
            case 'h' | 8: return H.getUnicode();
            case 'i' | 9: return I.getUnicode();
            case 'j' | 10: return J.getUnicode();
            case 'k' | 11: return K.getUnicode();
            case 'l' | 12: return L.getUnicode();
            case 'm' | 13: return M.getUnicode();
            case 'n' | 14: return N.getUnicode();
            case 'o' | 15: return O.getUnicode();
            case 'p' | 16: return P.getUnicode();
            case 'q' | 17: return Q.getUnicode();
            case 'r' | 18: return R.getUnicode();
            case 's' | 19: return S.getUnicode();
            case 't' | 20: return T.getUnicode();
            case 'u' | 21: return U.getUnicode();
            case 'v' | 22: return V.getUnicode();
            case 'w' | 23: return W.getUnicode();
            case 'x' | 24: return X.getUnicode();
            case 'y' | 25: return Y.getUnicode();
            case 'z' | 26: return Z.getUnicode();
            default: return null;
        }
    }

    public String getUnicode() {
        return unicode;
    }
}
