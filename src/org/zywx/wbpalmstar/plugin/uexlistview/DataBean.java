package org.zywx.wbpalmstar.plugin.uexlistview;

import java.util.List;

public class DataBean {
	private List<DateItem> dates;
	private SwipeOption sopLeft;
	private SwipeOption sopRight;

	public List<DateItem> getDates() {
		return dates;
	}

	public void setDates(List<DateItem> dates) {
		this.dates = dates;
	}
	
	public SwipeOption getSopLeft() {
		return sopLeft;
	}

	public void setSopLeft(SwipeOption sopLeft) {
		this.sopLeft = sopLeft;
	}

	public SwipeOption getSopRight() {
		return sopRight;
	}

	public void setSopRight(SwipeOption sopRight) {
		this.sopRight = sopRight;
	}


	public class SwipeOption  {
		private String backgroundColor;
		private List<ButtonItem> bts;
		
		
		
		public String getBackgroundColor() {
			return backgroundColor;
		}



		public void setBackgroundColor(String backgroundColor) {
			this.backgroundColor = backgroundColor;
		}



		public List<ButtonItem> getBts() {
			return bts;
		}



		public void setBts(List<ButtonItem> bts) {
			this.bts = bts;
		}



		public class ButtonItem {
			
			private String bgColor;
			private String btIndex;
			private String text;
			private String textColor;
			private String textSize;
			public String getBgColor() {
				return bgColor;
			}
			public void setBgColor(String bgColor) {
				this.bgColor = bgColor;
			}
			public String getBtIndex() {
				return btIndex;
			}
			public void setBtIndex(String btIndex) {
				this.btIndex = btIndex;
			}
			public String getText() {
				return text;
			}
			public void setText(String text) {
				this.text = text;
			}
			public String getTextColor() {
				return textColor;
			}
			public void setTextColor(String textColor) {
				this.textColor = textColor;
			}
			public String getTextSize() {
				return textSize;
			}
			public void setTextSize(String textSize) {
				this.textSize = textSize;
			}
			
		}
	}
	
	
	public class DateItem {

		private String title;
		private String titleColor;
		private String titleSize;
		private String subtitle;
		private String subtitleColor;
		private String subtitleSize;
		private String height;
		private String image;
		private String backgroundColor;
		private String selectedBackgroundColor;
		private String rightBtnImg;
		private String placeholderImg;
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getTitleColor() {
			return titleColor;
		}
		public void setTitleColor(String titleColor) {
			this.titleColor = titleColor;
		}
		public String getTitleSize() {
			return titleSize;
		}
		public void setTitleSize(String titleSize) {
			this.titleSize = titleSize;
		}
		public String getSubtitle() {
			return subtitle;
		}
		public void setSubtitle(String subtitle) {
			this.subtitle = subtitle;
		}
		public String getSubtitleColor() {
			return subtitleColor;
		}
		public void setSubtitleColor(String subtitleColor) {
			this.subtitleColor = subtitleColor;
		}
		public String getSubtitleSize() {
			return subtitleSize;
		}
		public void setSubtitleSize(String subtitleSize) {
			this.subtitleSize = subtitleSize;
		}
		public String getHeight() {
			return height;
		}
		public void setHeight(String height) {
			this.height = height;
		}
		public String getImage() {
			return image;
		}
		public void setImage(String image) {
			this.image = image;
		}
		public String getBackgroundColor() {
			return backgroundColor;
		}
		public void setBackgroundColor(String backgroundColor) {
			this.backgroundColor = backgroundColor;
		}
		public String getSelectedBackgroundColor() {
			return selectedBackgroundColor;
		}
		public void setSelectedBackgroundColor(String selectedBackgroundColor) {
			this.selectedBackgroundColor = selectedBackgroundColor;
		}
		public String getRightBtnImg() {
			return rightBtnImg;
		}
		public void setRightBtnImg(String rightBtnImg) {
			this.rightBtnImg = rightBtnImg;
		}
		public String getPlaceholderImg() {
			return placeholderImg;
		}
		public void setPlaceholderImg(String placeholderImg) {
			this.placeholderImg = placeholderImg;
		}


	}
}
