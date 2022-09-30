package uminho.dss.turmas3l.data;

import uminho.dss.turmas3l.business.Aluno;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AlunoDAO implements Map<String, Aluno>
{

    private static AlunoDAO singleton = null;

    private AlunoDAO()
    {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD); Statement stm = conn.createStatement())
        {
            String sql =    "CREATE TABLE IF NOT EXISTS alunos (" +
                            "Num varchar(10) NOT NULL PRIMARY KEY," +
                            "Nome varchar(45) DEFAULT NULL," +
                            "Email varchar(45) DEFAULT NULL," +
                            "Turma varchar(10), foreign key(Turma) references turmas(Id))";
            stm.executeUpdate(sql);
        }
        catch (SQLException e)
        {
            // Erro a criar tabela
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    public static AlunoDAO getInstance()
    {
        if (AlunoDAO.singleton == null)
        {
            AlunoDAO.singleton = new AlunoDAO();
        }
        return AlunoDAO.singleton;
    }

    /*
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT count(*) FROM alunos") )
        {
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }

     */

    @Override
    public int size() {
        int i = 0;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT count(*) FROM alunos") )
        {
            if (rs.next())
            {
                i = rs.getInt(1);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return i;
    }

    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        boolean r;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs =
                     stm.executeQuery("SELECT Num FROM alunos WHERE Num='"+key.toString()+"'"))
        {
            r = rs.next();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return r;
    }

    @Override
    public boolean containsValue(Object value)
    {
        Aluno aluno = (Aluno) value;
        Aluno a = this.get(aluno.getNumero());
        return aluno.equals(a);
    }

    @Override
    public Aluno get(Object key)
    {
        Aluno aluno = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs =
                     stm.executeQuery("SELECT * FROM alunos WHERE Num='"+key+"'") )
        {
            if (rs.next())
            {
                aluno = new Aluno (rs.getString("Num"), rs.getString("Nome"), rs.getString("Email"));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return aluno;
    }

    @Override
    public Aluno put(String key, Aluno value)
    {
        Aluno oldVal = this.get(key);
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement())
        {
            stm.executeUpdate(
                            "INSERT INTO alunos "+
                                        "VALUES ('"+ value.getNumero()+ "', '"+
                                                     value.getNome()+ "', '"+
                                                     value.getEmail()+"', NULL) " +
                                        "ON DUPLICATE KEY UPDATE Nome=Values(Nome), "+
                                                                 "Email=Values(Email)");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return oldVal;
    }

    @Override
    public Aluno remove(Object key)
    {
        Aluno a = this.get(key);
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement())
        {
            stm.executeUpdate("DELETE FROM alunos WHERE Num='"+key.toString()+"'");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return a;
    }

    @Override
    public void putAll(Map<? extends String, ? extends Aluno> m)
    {
        for (Aluno a : m.values())
            this.put(a.getNumero(), a);
    }

    @Override
    public void clear()
    {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement())
        {
            stm.executeUpdate("TRUNCATE alunos");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    @Override
    public Set<String> keySet() {
        return null;
    }

    @Override
    public Collection<Aluno> values()
    {
        Collection<Aluno> res = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT Num from alunos"))
        {
            while(rs.next())
            {
                String NumAluno = rs.getString("Num");
                Aluno a = this.get(NumAluno);
                res.add(a);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return res;
    }

    @Override
    public Set<Entry<String, Aluno>> entrySet() {
        return null;
    }
}
