package model.dao;

import model.dao.exception.DuplicatedObjectException;
import model.mo.Stanza;

public interface RoomDAO {

    Stanza insert(Stanza stanza) throws DuplicatedObjectException;

    boolean update(Stanza stanza);

    boolean delete(Stanza stanza);

    Stanza[] findAllRooms();

    Stanza findRoom(int roomId);

}
