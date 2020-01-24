package ca.sait.vezorla.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.sun.istack.NotNull;

import lombok.Data;

/**
 * Entity class for backup
 * @author		Minh Lam
 * @version		1.0
 * */

@Entity
@Data
@Table(name = "backup")
public class Backup {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@Column(name = "name")
	private String name;
	
	@Column(name = "description")
	private String description;
	
	@NotNull
	@Column(name = "date")
	private Date dateCreated;
}
