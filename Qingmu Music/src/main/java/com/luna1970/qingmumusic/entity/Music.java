package com.luna1970.qingmumusic.entity;

/**
 * 歌曲类
 *
 * @author ChengHeng@tarena.com.cn
 *
 */
public class Music {
	/**
	 * ID
	 */
	private int id;
	/**
	 * 路径，例如 /mnt/sdcard/Music/ff10.mp3
	 */
	private String data;
	/**
	 * 文件名，例如 ff10.mp3
	 */
	private String name;
	/**
	 * 标题（该数据为Mp3文件内置数据），例如 To Zanarkand
	 */
	private String title;
	/**
	 * 作者（该数据为Mp3文件内置数据），例如 Nobuo Uematsu
	 */
	private String artist;
	/**
	 * 专辑（该数据为Mp3文件内置数据），例如 Final Fantasy X OST CD 1
	 */
	private String album;
	/**
	 * 图片Key（该数据为Mp3文件内置数据）
	 */
	private long albumID;
	/**
	 * 图片路径（该数据为Mp3文件内置数据）
	 */
	private String albumArt;
	/**
	 * 歌曲时长，例如 184895
	 */
	private long duration;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		if(title == null || "".equals(title)) {
			return getName();
		}
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getArtist() {
		if(artist == null || "".equals(artist)) {
			return "未知作者";
		}
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getAlbum() {
		if(album == null || "".equals(album)) {
			return "未知专辑";
		}
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}


	public String getAlbumArt() {
		return albumArt;
	}

	public void setAlbumArt(String albumArt) {
		this.albumArt = albumArt;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

    public long getAlbumID() {
        return albumID;
    }

    public void setAlbumID(long albumID) {
        this.albumID = albumID;
    }

    @Override
    public String toString() {
        return "Music{" +
                "id=" + id +
                ", data='" + data + '\'' +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", album='" + album + '\'' +
                ", albumID=" + albumID +
                ", albumArt='" + albumArt + '\'' +
                ", duration=" + duration +
                '}';
    }
}
