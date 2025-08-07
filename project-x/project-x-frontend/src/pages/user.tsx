import {
  DeleteOutlined,
  EditOutlined,
  PlusCircleOutlined,
} from "@ant-design/icons";
import { Button, message, Popconfirm, Table } from "antd";
import { useEffect, useState } from "react";
import CreateUserModal from "../components/modal/create.user.modal";
import { deleteUserAPI, getUsersAPI } from "../services/api";
import UpdateUserModal from "../components/modal/update.user.modal";

interface IUser {
  id: number;
  name: string;
  email: string;
}

const UserPage = () => {
  const [users, setUsers] = useState<IUser[]>([]);
  const [userUpdate, setUserUpdate] = useState<IUser | null>(null);
  const [isOpenCreateModal, setIsOpenCreateModal] = useState(false);
  const [isOpenUpdateModal, setIsOpenUpdateModal] = useState(false);

  const fetchUsers = async () => {
    const res = await getUsersAPI();
    if (res?.data?.status === "success") {
      setUsers(res?.data?.data);
    }
  };

  const handleEdit = (data: IUser) => {
    setIsOpenUpdateModal(true);
    setUserUpdate(data);
  };

  const handleDelete = async (data: IUser) => {
    const res = await deleteUserAPI(data.id);
    if (res.data) {
      message.success("Xoá thành công!");
      await fetchUsers();
    }
  };

  useEffect(() => {
    fetchUsers();
  }, []);

  const columns = [
    {
      title: "ID",
      dataIndex: "id",
    },
    {
      title: "Name",
      dataIndex: "name",
    },
    {
      title: "Email",
      dataIndex: "email",
    },
    {
      title: "Action",
      render: (_: string, record: IUser) => {
        return (
          <>
            <Button
              onClick={() => handleEdit(record)}
              style={{ color: "orange", marginRight: 10, cursor: "pointer" }}
              icon={<EditOutlined />}
            >
              Edit
            </Button>
            <Popconfirm
              title="Delete the user"
              description="Are you sure to delete this user?"
              onConfirm={() => handleDelete(record)}
              // onCancel={cancel}
              okText="Yes"
              cancelText="No"
            >
              <Button
                onClick={() => handleEdit}
                style={{ color: "red", marginRight: 10, cursor: "pointer" }}
                icon={<DeleteOutlined />}
              >
                Delete
              </Button>
            </Popconfirm>
          </>
        );
      },
    },
  ];

  return (
    <div style={{ padding: 10 }}>
      <div
        style={{
          display: "flex",
          justifyContent: "space-between",
          alignItems: "center",
        }}
      >
        <h3>Table Users</h3>
        <Button
          onClick={() => setIsOpenCreateModal(true)}
          type="primary"
          icon={<PlusCircleOutlined />}
        >
          ADD
        </Button>
      </div>
      <Table bordered dataSource={users} columns={columns} rowKey={"id"} />
      <CreateUserModal
        isOpenCreateModal={isOpenCreateModal}
        setIsOpenCreateModal={setIsOpenCreateModal}
        fetchUsers={fetchUsers}
      />
      <UpdateUserModal
        isOpenUpdateModal={isOpenUpdateModal}
        setIsOpenUpdateModal={setIsOpenUpdateModal}
        fetchUsers={fetchUsers}
        userUpdate={userUpdate}
        setUserUpdate={setUserUpdate}
      />
    </div>
  );
};

export default UserPage;
