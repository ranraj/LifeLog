import { IEventLog } from 'app/entities/event-log/event-log.model';

export interface IEventLogType {
  id?: number;
  name?: string | null;
  template?: string | null;
  eventLog?: IEventLog | null;
}

export class EventLogType implements IEventLogType {
  constructor(public id?: number, public name?: string | null, public template?: string | null, public eventLog?: IEventLog | null) {}
}

export function getEventLogTypeIdentifier(eventLogType: IEventLogType): number | undefined {
  return eventLogType.id;
}
