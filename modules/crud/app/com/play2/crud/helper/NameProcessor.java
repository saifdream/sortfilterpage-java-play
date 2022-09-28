package com.play2.crud.helper;

public class NameProcessor {

	private static final int CHAR_UPPER = 1;
	private static final int CHAR_LOWER = 2;
	private static final int CHAR_DIGIT = 3;
	private static final int CHAR_SPECIAL = 4;

	private static int getCharType(char ch) {
		if (Character.isUpperCase(ch)) {
			return CHAR_UPPER;
		} else if (Character.isLowerCase(ch)) {
			return CHAR_LOWER;
		} else if (Character.isDigit(ch)) {
			return CHAR_DIGIT;
		} else {
			return CHAR_SPECIAL;
		}
	}

	private static boolean spaceRequired(int currCharPosition, String camelCase) {
		if (currCharPosition == 0)
			return false;

		char currChar = camelCase.charAt(currCharPosition);
		char prevChar = camelCase.charAt(currCharPosition - 1);

		// Ax -> current character x
		if (Character.isUpperCase(prevChar) && Character.isLowerCase(currChar))
			return false;

		// AXc -> current character X
		if (currCharPosition != (camelCase.length() - 1)) { // ** not the last character
			char nextChar = camelCase.charAt(currCharPosition + 1);
			if (Character.isUpperCase(currChar) && Character.isUpperCase(prevChar) && Character.isLowerCase(nextChar))
				return true;
		}

		if (getCharType(prevChar) != getCharType(currChar))
			return true;

		return false;
	}

	public static String getLabel(String label, String name) {
		if (label == null || "".equals(label))
			return toLabelCase(name);

		return label;
	}

	public static String toLabelCase(String camelCase) {
		if (camelCase == null || camelCase.trim().isEmpty())
			throw new IllegalArgumentException("Provided string is either null or contains only whitespace");

		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < camelCase.length(); i++) {
			char c = camelCase.charAt(i);

			if ('_' == c) { // ** Underscores should be converted to space
				sb.append(" ");
			} else if (spaceRequired(i, camelCase)) {
				sb.append(" " + Character.toUpperCase(c));
			} else {
				sb.append(c);
			}
		}

		String label = sb.toString().trim().replaceAll(" +", " ");
		if (label.isEmpty())
			throw new IllegalArgumentException("Provided string (" + camelCase + ") does not contain any meaningful letter.");

		return beautify(toSentenceCase(formatId(label)));
	}

	public static String toSentenceCase(String input) {
		if (input == null || "".equals(input.trim()))
			return "";

		if (input.length() == 1)
			return input.toUpperCase();

		return (input.substring(0, 1).toUpperCase() + input.substring(1));
	}

	public static String toCamelCase(String input) {
		if (input == null || "".equals(input.trim()))
			return "";

		// TODO improve for underscore character
		if (input.length() == 1)
			return input.toLowerCase();

		return (input.substring(0, 1).toLowerCase() + input.substring(1));
	}
	
	
	public static String toUrlCase(String input) {
		if (input == null || "".equals(input.trim()))
			return "";

		if (input.length() == 1)
			return input.toLowerCase();

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);

			if (!Character.isLetter(c)) {
				sb.append("-");
			} else if (Character.isUpperCase(c)) {
				sb.append("-" + c);
			} else {
				sb.append(c);
			}
		}

		String output = sb.toString().toLowerCase().replaceAll("-+","-").replaceAll("\\s","");
		if(output.startsWith("-"))
			output = output.substring(1);
		if(output.endsWith("-"))
			output = output.substring(0, output.length()-1);
		
		return output;
	}

	
	public static String getInitials(String input) {
		String output = toSentenceCase(input);
		
		StringBuilder sb = new StringBuilder();
		for (char c : output.toCharArray()) {
			if (Character.isUpperCase(c))
				sb.append(c);
		}
		
		return sb.toString().toLowerCase();
	}
	
	private static String beautify(String input) {
		if(input.startsWith("Is "))
			return input.substring(3)+"?";
		
		return input;
	}

	public static String formatId(String input) {
		String output = input.replaceAll(" Id ", " ID ");
		if (output.endsWith(" Id"))
			output = output.substring(0, output.length() - 3) + " ID";
		if (output.startsWith("Id "))
			output = "ID " + output.substring(3);
		if ("Id".equals(output) || "id".equals(output))
			output = "ID";

		return output;
	}
}
