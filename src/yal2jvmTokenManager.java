/* yal2jvmTokenManager.java */
/* Generated By:JavaCC: Do not edit this line. yal2jvmTokenManager.java */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/** Token Manager. */
public class yal2jvmTokenManager implements yal2jvmConstants {

  /** Debug output. */
  public static  java.io.PrintStream debugStream = System.out;
  /** Set debug output. */
  public static  void setDebugStream(java.io.PrintStream ds) { debugStream = ds; }
private static final int jjStopStringLiteralDfa_0(int pos, long active0){
   switch (pos)
   {
      case 0:
         if ((active0 & 0x10000L) != 0L)
            return 11;
         if ((active0 & 0x8800L) != 0L)
            return 1;
         if ((active0 & 0x3807000L) != 0L)
         {
            jjmatchedKind = 27;
            return 9;
         }
         return -1;
      case 1:
         if ((active0 & 0x2000L) != 0L)
            return 9;
         if ((active0 & 0x3805000L) != 0L)
         {
            jjmatchedKind = 27;
            jjmatchedPos = 1;
            return 9;
         }
         return -1;
      case 2:
         if ((active0 & 0x3805000L) != 0L)
         {
            jjmatchedKind = 27;
            jjmatchedPos = 2;
            return 9;
         }
         return -1;
      case 3:
         if ((active0 & 0x1801000L) != 0L)
         {
            jjmatchedKind = 27;
            jjmatchedPos = 3;
            return 9;
         }
         if ((active0 & 0x2004000L) != 0L)
            return 9;
         return -1;
      case 4:
         if ((active0 & 0x1800000L) != 0L)
         {
            jjmatchedKind = 27;
            jjmatchedPos = 4;
            return 9;
         }
         if ((active0 & 0x1000L) != 0L)
            return 9;
         return -1;
      case 5:
         if ((active0 & 0x800000L) != 0L)
         {
            jjmatchedKind = 27;
            jjmatchedPos = 5;
            return 9;
         }
         if ((active0 & 0x1000000L) != 0L)
            return 9;
         return -1;
      case 6:
         if ((active0 & 0x800000L) != 0L)
         {
            jjmatchedKind = 27;
            jjmatchedPos = 6;
            return 9;
         }
         return -1;
      default :
         return -1;
   }
}
private static final int jjStartNfa_0(int pos, long active0){
   return jjMoveNfa_0(jjStopStringLiteralDfa_0(pos, active0), pos + 1);
}
static private int jjStopAtPos(int pos, int kind)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   return pos + 1;
}
static private int jjMoveStringLiteralDfa0_0(){
   switch(curChar)
   {
      case 33:
         return jjStartNfaWithStates_0(0, 11, 1);
      case 34:
         return jjStartNfaWithStates_0(0, 16, 11);
      case 40:
         return jjStopAtPos(0, 17);
      case 41:
         return jjStopAtPos(0, 18);
      case 44:
         return jjStopAtPos(0, 19);
      case 46:
         return jjStopAtPos(0, 33);
      case 59:
         return jjStopAtPos(0, 20);
      case 61:
         return jjStartNfaWithStates_0(0, 15, 1);
      case 91:
         return jjStopAtPos(0, 31);
      case 93:
         return jjStopAtPos(0, 32);
      case 101:
         return jjMoveStringLiteralDfa1_0(0x4000L);
      case 102:
         return jjMoveStringLiteralDfa1_0(0x800000L);
      case 105:
         return jjMoveStringLiteralDfa1_0(0x2000L);
      case 109:
         return jjMoveStringLiteralDfa1_0(0x1000000L);
      case 115:
         return jjMoveStringLiteralDfa1_0(0x2000000L);
      case 119:
         return jjMoveStringLiteralDfa1_0(0x1000L);
      case 123:
         return jjStopAtPos(0, 21);
      case 125:
         return jjStopAtPos(0, 22);
      default :
         return jjMoveNfa_0(0, 0);
   }
}
static private int jjMoveStringLiteralDfa1_0(long active0){
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(0, active0);
      return 1;
   }
   switch(curChar)
   {
      case 102:
         if ((active0 & 0x2000L) != 0L)
            return jjStartNfaWithStates_0(1, 13, 9);
         break;
      case 104:
         return jjMoveStringLiteralDfa2_0(active0, 0x1000L);
      case 105:
         return jjMoveStringLiteralDfa2_0(active0, 0x2000000L);
      case 108:
         return jjMoveStringLiteralDfa2_0(active0, 0x4000L);
      case 111:
         return jjMoveStringLiteralDfa2_0(active0, 0x1000000L);
      case 117:
         return jjMoveStringLiteralDfa2_0(active0, 0x800000L);
      default :
         break;
   }
   return jjStartNfa_0(0, active0);
}
static private int jjMoveStringLiteralDfa2_0(long old0, long active0){
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(0, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(1, active0);
      return 2;
   }
   switch(curChar)
   {
      case 100:
         return jjMoveStringLiteralDfa3_0(active0, 0x1000000L);
      case 105:
         return jjMoveStringLiteralDfa3_0(active0, 0x1000L);
      case 110:
         return jjMoveStringLiteralDfa3_0(active0, 0x800000L);
      case 115:
         return jjMoveStringLiteralDfa3_0(active0, 0x4000L);
      case 122:
         return jjMoveStringLiteralDfa3_0(active0, 0x2000000L);
      default :
         break;
   }
   return jjStartNfa_0(1, active0);
}
static private int jjMoveStringLiteralDfa3_0(long old0, long active0){
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(1, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(2, active0);
      return 3;
   }
   switch(curChar)
   {
      case 99:
         return jjMoveStringLiteralDfa4_0(active0, 0x800000L);
      case 101:
         if ((active0 & 0x4000L) != 0L)
            return jjStartNfaWithStates_0(3, 14, 9);
         else if ((active0 & 0x2000000L) != 0L)
            return jjStartNfaWithStates_0(3, 25, 9);
         break;
      case 108:
         return jjMoveStringLiteralDfa4_0(active0, 0x1000L);
      case 117:
         return jjMoveStringLiteralDfa4_0(active0, 0x1000000L);
      default :
         break;
   }
   return jjStartNfa_0(2, active0);
}
static private int jjMoveStringLiteralDfa4_0(long old0, long active0){
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(2, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(3, active0);
      return 4;
   }
   switch(curChar)
   {
      case 101:
         if ((active0 & 0x1000L) != 0L)
            return jjStartNfaWithStates_0(4, 12, 9);
         break;
      case 108:
         return jjMoveStringLiteralDfa5_0(active0, 0x1000000L);
      case 116:
         return jjMoveStringLiteralDfa5_0(active0, 0x800000L);
      default :
         break;
   }
   return jjStartNfa_0(3, active0);
}
static private int jjMoveStringLiteralDfa5_0(long old0, long active0){
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(3, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(4, active0);
      return 5;
   }
   switch(curChar)
   {
      case 101:
         if ((active0 & 0x1000000L) != 0L)
            return jjStartNfaWithStates_0(5, 24, 9);
         break;
      case 105:
         return jjMoveStringLiteralDfa6_0(active0, 0x800000L);
      default :
         break;
   }
   return jjStartNfa_0(4, active0);
}
static private int jjMoveStringLiteralDfa6_0(long old0, long active0){
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(4, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(5, active0);
      return 6;
   }
   switch(curChar)
   {
      case 111:
         return jjMoveStringLiteralDfa7_0(active0, 0x800000L);
      default :
         break;
   }
   return jjStartNfa_0(5, active0);
}
static private int jjMoveStringLiteralDfa7_0(long old0, long active0){
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(5, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(6, active0);
      return 7;
   }
   switch(curChar)
   {
      case 110:
         if ((active0 & 0x800000L) != 0L)
            return jjStartNfaWithStates_0(7, 23, 9);
         break;
      default :
         break;
   }
   return jjStartNfa_0(6, active0);
}
static private int jjStartNfaWithStates_0(int pos, int kind, int state)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) { return pos + 1; }
   return jjMoveNfa_0(state, pos + 1);
}
static final long[] jjbitVec0 = {
   0x0L, 0x0L, 0xffffffffffffffffL, 0xffffffffffffffffL
};
static private int jjMoveNfa_0(int startState, int curPos)
{
   int startsAt = 0;
   jjnewStateCnt = 30;
   int i = 1;
   jjstateSet[0] = startState;
   int kind = 0x7fffffff;
   for (;;)
   {
      if (++jjround == 0x7fffffff)
         ReInitRounds();
      if (curChar < 64)
      {
         long l = 1L << curChar;
         do
         {
            switch(jjstateSet[--i])
            {
               case 0:
                  if ((0x3ff000000000000L & l) != 0L)
                  {
                     if (kind > 26)
                        kind = 26;
                     { jjCheckNAdd(7); }
                  }
                  else if ((0x840000000000L & l) != 0L)
                  {
                     if (kind > 9)
                        kind = 9;
                  }
                  else if ((0x280000000000L & l) != 0L)
                  {
                     if (kind > 8)
                        kind = 8;
                  }
                  else if ((0x5000000000000000L & l) != 0L)
                  {
                     if (kind > 7)
                        kind = 7;
                  }
                  else if (curChar == 34)
                     { jjCheckNAdd(11); }
                  else if (curChar == 36)
                  {
                     if (kind > 27)
                        kind = 27;
                     { jjCheckNAdd(9); }
                  }
                  else if (curChar == 38)
                  {
                     if (kind > 10)
                        kind = 10;
                  }
                  else if (curChar == 33)
                     { jjCheckNAdd(1); }
                  else if (curChar == 61)
                     { jjCheckNAdd(1); }
                  if (curChar == 62)
                     { jjCheckNAddStates(0, 2); }
                  else if (curChar == 60)
                     { jjCheckNAddTwoStates(1, 26); }
                  else if (curChar == 47)
                     { jjAddStates(3, 4); }
                  break;
               case 1:
                  if (curChar == 61 && kind > 7)
                     kind = 7;
                  break;
               case 2:
                  if (curChar == 61)
                     { jjCheckNAdd(1); }
                  break;
               case 3:
                  if (curChar == 33)
                     { jjCheckNAdd(1); }
                  break;
               case 4:
                  if ((0x280000000000L & l) != 0L && kind > 8)
                     kind = 8;
                  break;
               case 5:
                  if ((0x840000000000L & l) != 0L && kind > 9)
                     kind = 9;
                  break;
               case 6:
                  if (curChar == 38 && kind > 10)
                     kind = 10;
                  break;
               case 7:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 26)
                     kind = 26;
                  { jjCheckNAdd(7); }
                  break;
               case 8:
                  if (curChar != 36)
                     break;
                  if (kind > 27)
                     kind = 27;
                  { jjCheckNAdd(9); }
                  break;
               case 9:
                  if ((0x3ff001000000000L & l) == 0L)
                     break;
                  if (kind > 27)
                     kind = 27;
                  { jjCheckNAdd(9); }
                  break;
               case 10:
                  if (curChar == 34)
                     { jjCheckNAdd(11); }
                  break;
               case 11:
                  if ((0x27ff000100000000L & l) != 0L)
                     { jjCheckNAddTwoStates(11, 12); }
                  break;
               case 12:
                  if (curChar == 34 && kind > 30)
                     kind = 30;
                  break;
               case 13:
                  if (curChar == 47)
                     { jjAddStates(3, 4); }
                  break;
               case 14:
                  if (curChar == 47)
                     { jjCheckNAddStates(5, 7); }
                  break;
               case 15:
                  if ((0xffffffffffffdbffL & l) != 0L)
                     { jjCheckNAddStates(5, 7); }
                  break;
               case 16:
                  if ((0x2400L & l) != 0L && kind > 5)
                     kind = 5;
                  break;
               case 17:
                  if (curChar == 10 && kind > 5)
                     kind = 5;
                  break;
               case 18:
                  if (curChar == 13)
                     jjstateSet[jjnewStateCnt++] = 17;
                  break;
               case 19:
                  if (curChar == 42)
                     { jjCheckNAddTwoStates(20, 21); }
                  break;
               case 20:
                  if ((0xfffffbffffffffffL & l) != 0L)
                     { jjCheckNAddTwoStates(20, 21); }
                  break;
               case 21:
                  if (curChar == 42)
                     { jjCheckNAddStates(8, 10); }
                  break;
               case 22:
                  if ((0xffff7bffffffffffL & l) != 0L)
                     { jjCheckNAddTwoStates(23, 21); }
                  break;
               case 23:
                  if ((0xfffffbffffffffffL & l) != 0L)
                     { jjCheckNAddTwoStates(23, 21); }
                  break;
               case 24:
                  if (curChar == 47 && kind > 6)
                     kind = 6;
                  break;
               case 25:
                  if (curChar == 60)
                     { jjCheckNAddTwoStates(1, 26); }
                  break;
               case 26:
                  if (curChar == 60 && kind > 9)
                     kind = 9;
                  break;
               case 27:
                  if (curChar == 62)
                     { jjCheckNAddStates(0, 2); }
                  break;
               case 28:
                  if (curChar == 62 && kind > 9)
                     kind = 9;
                  break;
               case 29:
                  if (curChar == 62)
                     { jjCheckNAdd(28); }
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else if (curChar < 128)
      {
         long l = 1L << (curChar & 077);
         do
         {
            switch(jjstateSet[--i])
            {
               case 0:
                  if ((0x7fffffe87fffffeL & l) != 0L)
                  {
                     if (kind > 27)
                        kind = 27;
                     { jjCheckNAdd(9); }
                  }
                  else if ((0x1000000040000000L & l) != 0L)
                  {
                     if (kind > 10)
                        kind = 10;
                  }
                  break;
               case 6:
                  if ((0x1000000040000000L & l) != 0L && kind > 10)
                     kind = 10;
                  break;
               case 8:
               case 9:
                  if ((0x7fffffe87fffffeL & l) == 0L)
                     break;
                  if (kind > 27)
                     kind = 27;
                  { jjCheckNAdd(9); }
                  break;
               case 11:
                  if ((0x7fffffe07fffffeL & l) != 0L)
                     { jjAddStates(11, 12); }
                  break;
               case 15:
                  { jjAddStates(5, 7); }
                  break;
               case 20:
                  { jjCheckNAddTwoStates(20, 21); }
                  break;
               case 22:
               case 23:
                  { jjCheckNAddTwoStates(23, 21); }
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else
      {
         int i2 = (curChar & 0xff) >> 6;
         long l2 = 1L << (curChar & 077);
         do
         {
            switch(jjstateSet[--i])
            {
               case 15:
                  if ((jjbitVec0[i2] & l2) != 0L)
                     { jjAddStates(5, 7); }
                  break;
               case 20:
                  if ((jjbitVec0[i2] & l2) != 0L)
                     { jjCheckNAddTwoStates(20, 21); }
                  break;
               case 22:
               case 23:
                  if ((jjbitVec0[i2] & l2) != 0L)
                     { jjCheckNAddTwoStates(23, 21); }
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      if (kind != 0x7fffffff)
      {
         jjmatchedKind = kind;
         jjmatchedPos = curPos;
         kind = 0x7fffffff;
      }
      ++curPos;
      if ((i = jjnewStateCnt) == (startsAt = 30 - (jjnewStateCnt = startsAt)))
         return curPos;
      try { curChar = input_stream.readChar(); }
      catch(java.io.IOException e) { return curPos; }
   }
}

/** Token literal values. */
public static final String[] jjstrLiteralImages = {
"", null, null, null, null, null, null, null, null, null, null, "\41", 
"\167\150\151\154\145", "\151\146", "\145\154\163\145", "\75", "\42", "\50", "\51", "\54", "\73", 
"\173", "\175", "\146\165\156\143\164\151\157\156", "\155\157\144\165\154\145", 
"\163\151\172\145", null, null, null, null, null, "\133", "\135", "\56", };
static protected Token jjFillToken()
{
   final Token t;
   final String curTokenImage;
   final int beginLine;
   final int endLine;
   final int beginColumn;
   final int endColumn;
   String im = jjstrLiteralImages[jjmatchedKind];
   curTokenImage = (im == null) ? input_stream.GetImage() : im;
   beginLine = input_stream.getBeginLine();
   beginColumn = input_stream.getBeginColumn();
   endLine = input_stream.getEndLine();
   endColumn = input_stream.getEndColumn();
   t = Token.newToken(jjmatchedKind, curTokenImage);

   t.beginLine = beginLine;
   t.endLine = endLine;
   t.beginColumn = beginColumn;
   t.endColumn = endColumn;

   return t;
}
static final int[] jjnextStates = {
   1, 28, 29, 14, 19, 15, 16, 18, 21, 22, 24, 11, 12, 
};

static int curLexState = 0;
static int defaultLexState = 0;
static int jjnewStateCnt;
static int jjround;
static int jjmatchedPos;
static int jjmatchedKind;

/** Get the next Token. */
public static Token getNextToken() 
{
  Token matchedToken;
  int curPos = 0;

  EOFLoop :
  for (;;)
  {
   try
   {
      curChar = input_stream.BeginToken();
   }
   catch(Exception e)
   {
      jjmatchedKind = 0;
      jjmatchedPos = -1;
      matchedToken = jjFillToken();
      return matchedToken;
   }

   try { input_stream.backup(0);
      while (curChar <= 32 && (0x100002600L & (1L << curChar)) != 0L)
         curChar = input_stream.BeginToken();
   }
   catch (java.io.IOException e1) { continue EOFLoop; }
   jjmatchedKind = 0x7fffffff;
   jjmatchedPos = 0;
   curPos = jjMoveStringLiteralDfa0_0();
   if (jjmatchedKind != 0x7fffffff)
   {
      if (jjmatchedPos + 1 < curPos)
         input_stream.backup(curPos - jjmatchedPos - 1);
      if ((jjtoToken[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L)
      {
         matchedToken = jjFillToken();
         return matchedToken;
      }
      else
      {
         continue EOFLoop;
      }
   }
   int error_line = input_stream.getEndLine();
   int error_column = input_stream.getEndColumn();
   String error_after = null;
   boolean EOFSeen = false;
   try { input_stream.readChar(); input_stream.backup(1); }
   catch (java.io.IOException e1) {
      EOFSeen = true;
      error_after = curPos <= 1 ? "" : input_stream.GetImage();
      if (curChar == '\n' || curChar == '\r') {
         error_line++;
         error_column = 0;
      }
      else
         error_column++;
   }
   if (!EOFSeen) {
      input_stream.backup(1);
      error_after = curPos <= 1 ? "" : input_stream.GetImage();
   }
   throw new TokenMgrError(EOFSeen, curLexState, error_line, error_column, error_after, curChar, TokenMgrError.LEXICAL_ERROR);
  }
}

static void SkipLexicalActions(Token matchedToken)
{
   switch(jjmatchedKind)
   {
      default :
         break;
   }
}
static void MoreLexicalActions()
{
   jjimageLen += (lengthOfMatch = jjmatchedPos + 1);
   switch(jjmatchedKind)
   {
      default :
         break;
   }
}
static void TokenLexicalActions(Token matchedToken)
{
   switch(jjmatchedKind)
   {
      default :
         break;
   }
}
static private void jjCheckNAdd(int state)
{
   if (jjrounds[state] != jjround)
   {
      jjstateSet[jjnewStateCnt++] = state;
      jjrounds[state] = jjround;
   }
}
static private void jjAddStates(int start, int end)
{
   do {
      jjstateSet[jjnewStateCnt++] = jjnextStates[start];
   } while (start++ != end);
}
static private void jjCheckNAddTwoStates(int state1, int state2)
{
   jjCheckNAdd(state1);
   jjCheckNAdd(state2);
}

static private void jjCheckNAddStates(int start, int end)
{
   do {
      jjCheckNAdd(jjnextStates[start]);
   } while (start++ != end);
}

    /** Constructor. */
    public yal2jvmTokenManager(SimpleCharStream stream){

      if (input_stream != null)
        throw new TokenMgrError("ERROR: Second call to constructor of static lexer. You must use ReInit() to initialize the static variables.", TokenMgrError.STATIC_LEXER_ERROR);

    input_stream = stream;
  }

  /** Constructor. */
  public yal2jvmTokenManager (SimpleCharStream stream, int lexState){
    ReInit(stream);
    SwitchTo(lexState);
  }

  /** Reinitialise parser. */
  
  static public void ReInit(SimpleCharStream stream)
  {


    jjmatchedPos =
    jjnewStateCnt =
    0;
    curLexState = defaultLexState;
    input_stream = stream;
    ReInitRounds();
  }

  static private void ReInitRounds()
  {
    int i;
    jjround = 0x80000001;
    for (i = 30; i-- > 0;)
      jjrounds[i] = 0x80000000;
  }

  /** Reinitialise parser. */
  static public void ReInit(SimpleCharStream stream, int lexState)
  
  {
    ReInit(stream);
    SwitchTo(lexState);
  }

  /** Switch to specified lex state. */
  public static void SwitchTo(int lexState)
  {
    if (lexState >= 1 || lexState < 0)
      throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", TokenMgrError.INVALID_LEXICAL_STATE);
    else
      curLexState = lexState;
  }


/** Lexer state names. */
public static final String[] lexStateNames = {
   "DEFAULT",
};

/** Lex State array. */
public static final int[] jjnewLexState = {
   -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
   -1, -1, -1, -1, -1, -1, -1, -1, -1, 
};
static final long[] jjtoToken = {
   0x3cfffff81L, 
};
static final long[] jjtoSkip = {
   0x7eL, 
};
static final long[] jjtoSpecial = {
   0x0L, 
};
static final long[] jjtoMore = {
   0x0L, 
};
    static protected SimpleCharStream  input_stream;

    static private final int[] jjrounds = new int[30];
    static private final int[] jjstateSet = new int[2 * 30];
    private static final StringBuilder jjimage = new StringBuilder();
    private static StringBuilder image = jjimage;
    private static int jjimageLen;
    private static int lengthOfMatch;
    static protected int curChar;
}
