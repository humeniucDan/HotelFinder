package HotelFinder.hotelFinder.DAO;

import HotelFinder.hotelFinder.DBConnection.ConnectionFactory;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

public class AbstractDAO<t>{
    private final Class<t> type;

    @SuppressWarnings("unchecked")
    public AbstractDAO(){
        this.type = (Class<t>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @SuppressWarnings("unchecked")
    public List<t> createObjects(ResultSet resultSet) {
        List<t> retList = new LinkedList<>();
        System.out.println(type.getSimpleName());

        Constructor<t>[] constructors = (Constructor<t>[]) type.getDeclaredConstructors();
        Constructor<t> constructor = null;
        for(Constructor<t> constructorIterator : constructors){
            constructor = constructorIterator;
            if(constructor.getGenericParameterTypes().length == 0) break;
        }
        if(constructor == null) throw new NullPointerException();

        try {
            while(resultSet.next()){
                constructor.setAccessible(true);
                t newInstance = constructor.newInstance();

                for(Field field : type.getDeclaredFields()){
                    String fieldName = field.getName();
                    Object fieldValue = resultSet.getObject(fieldName);
                    new PropertyDescriptor(fieldName, type)
                            .getWriteMethod()
                            .invoke(newInstance, fieldValue);
                }
                retList.add(newInstance);
            }
        } catch(Exception e) { e.printStackTrace();}

        return retList;
    }

    private String createSelectQuery(ObjectFilter filter) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM [");
        sb.append(type.getSimpleName());
        sb.append("]");
        if(filter != null){
            sb.append(" WHERE ").append("1 = 1");
            sb.append(filter.generateConditionQuery());
        }
        sb.append(";");
        return sb.toString();
    }

    public List<t> findAll() {
        return findAll(null);
    }

    public List<t> findAll(ObjectFilter filter) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectQuery(filter);
        System.out.println(query);
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            return createObjects(resultSet);

        } catch (Exception e) { e.printStackTrace();
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    String createInsertQuery(t t) {
        StringBuilder insertQuery = new StringBuilder("INSERT INTO [");
        insertQuery.append(type.getSimpleName()).append("] (");
        StringBuilder valuesStr = new StringBuilder("VALUES (");

        try {
            for(Field field : type.getDeclaredFields()) {
                insertQuery.append(field.getName()).append(", ");
                valuesStr
                        .append("'")
                        .append(new PropertyDescriptor(field.getName(), type).getReadMethod().invoke(t))
                        .append("', ");
            }
            insertQuery.replace(insertQuery.length() - 2, insertQuery.length(),  ") ");
            valuesStr.replace(valuesStr.length() - 2, valuesStr.length(),  ");");

            insertQuery.append(valuesStr);
            return insertQuery.toString();

        } catch(Exception  e) { e.printStackTrace();}

        return null;
    }

    public t insert(t t) {
        Connection connection = null;
        PreparedStatement statement = null;
        String insertQuery = createInsertQuery(t);
        if(insertQuery == null) return null;

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(insertQuery);
            System.out.println(insertQuery);
            statement.execute();

            return t;
        } catch (Exception e) { e.printStackTrace();
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    public String createUpdateQuery(t t){
        StringBuilder updateQuery = new StringBuilder("UPDATE [").append(type.getSimpleName()).append("] SET ");

        try {
            Object idValue = null;
            for (Field field : type.getDeclaredFields()) {
                if (field.getName().equals("id")) {
                    idValue = new PropertyDescriptor(field.getName(), type).getReadMethod().invoke(t);
                }
                else {
                    updateQuery.append(field.getName()).append(" = ");
                    updateQuery
                            .append("'")
                            .append(new PropertyDescriptor(field.getName(), type).getReadMethod().invoke(t))
                            .append("', ");
                }
            }

            updateQuery
                    .replace(updateQuery.length()-2, updateQuery.length()," where id = '")
                    .append(idValue)
                    .append("';");

            return updateQuery.toString();

        } catch(Exception e) { e.printStackTrace();}
        return null;
    }
    public t update(t t) {
        Connection connection = null;
        PreparedStatement statement = null;
        String updateQuery = createUpdateQuery(t);
        if(updateQuery == null) return null;

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(updateQuery);
            System.out.println(updateQuery);
            statement.execute();

            return t;
        } catch (Exception e) { e.printStackTrace();
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    public String createRemoveQuery(t t){
        StringBuilder removeQuery = new StringBuilder("DELETE FROM [").append(type.getSimpleName()).append("] WHERE ");

        try {
            for (Field field : type.getDeclaredFields()) {
                removeQuery
                        .append(field.getName())
                        .append(" = '")
                        .append(new PropertyDescriptor(field.getName(), type).getReadMethod().invoke(t))
                        .append("' and ");
            }
            removeQuery.replace(removeQuery.length() - 4, removeQuery.length(), ";");

            System.out.println(removeQuery);

            return removeQuery.toString();

        } catch(Exception e) { e.printStackTrace();}
        return null;
    }
    public t remove(t t) {
        Connection connection = null;
        PreparedStatement statement = null;
        String updateQuery = createRemoveQuery(t);
        if(updateQuery == null) return null;

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(updateQuery);
            statement.execute();

            return t;
        } catch (Exception e) { e.printStackTrace();
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }
}

